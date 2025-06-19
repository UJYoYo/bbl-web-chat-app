import { useEffect, useState, useRef } from 'react';
import { messageAPI } from './Api.jsx';

function ChatRoom({ roomId, friendUsername }) {
    const [history, setHistory] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [isConnected, setIsConnected] = useState(false);
    const [connectionStatus, setConnectionStatus] = useState('Connecting...');

    const ws = useRef(null);
    const messagesEndRef = useRef(null);

    const current_username = localStorage.getItem("username");
    const current_userId = localStorage.getItem("userId");

    console.log("chatroom room ID:", roomId);
    console.log("friendusername:", friendUsername);
    console.log("current user ID:", current_userId);

    // Scroll to bottom of messages
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useEffect(() => {
        scrollToBottom();
    }, [history]);

    // Fetch message history
    const fetchHistory = async () => {
        try {
            const tmp_history = await messageAPI.getHistory(roomId);
            if (tmp_history) {
                console.log("tmp-history", tmp_history);
                setHistory(tmp_history);
            } else {
                setHistory([]);
            }
        } catch (e) {
            console.log("error chat room: ", e);
            setHistory([]);
        }
    };

    // Initialize WebSocket connection
    const initializeWebSocket = () => {
        try {
            // Replace with your WebSocket server URL
            const wsUrl = `ws://localhost:1234/ws?roomId=${roomId}&userId=${current_userId}&username=${current_username}`;
            ws.current = new WebSocket(wsUrl);

            ws.current.onopen = () => {
                console.log('WebSocket connected');
                setIsConnected(true);
                setConnectionStatus('Connected');

                // Join the room
                ws.current.send(JSON.stringify({
                    type: 'join_room',
                    roomId: roomId,
                    userId: current_userId,
                    username: current_username
                }));
            };

            ws.current.onmessage = (event) => {
                try {
                    const data = JSON.parse(event.data);
                    console.log('Received WebSocket message:', data);

                    switch (data.type) {
                        case 'new_message':
                            // Add new message to history
                            setHistory(prev => [...prev, {
                                messageId: data.messageId,
                                senderId: data.senderId,
                                content: data.content,
                                timestamp: data.timestamp,
                                senderUsername: data.senderUsername
                            }]);
                            break;

                        case 'message_history':
                            // Set initial message history
                            setHistory(data.messages || []);
                            break;

                        case 'user_joined':
                            console.log(`${data.username} joined the room`);
                            setConnectionStatus(`Connected - ${data.username} online`);
                            break;

                        case 'user_left':
                            console.log(`${data.username} left the room`);
                            setConnectionStatus('Connected');
                            break;

                        case 'error':
                            console.error('WebSocket error:', data.message);
                            break;

                        default:
                            console.log('Unknown message type:', data.type);
                    }
                } catch (error) {
                    console.error('Error parsing WebSocket message:', error);
                }
            };

            ws.current.onclose = () => {
                console.log('WebSocket disconnected');
                setIsConnected(false);
                setConnectionStatus('Disconnected');

                // Attempt to reconnect after 3 seconds
                setTimeout(() => {
                    if (roomId && current_userId) {
                        console.log('Attempting to reconnect...');
                        setConnectionStatus('Reconnecting...');
                        initializeWebSocket();
                    }
                }, 3000);
            };

            ws.current.onerror = (error) => {
                console.error('WebSocket error:', error);
                setConnectionStatus('Connection Error');
            };

        } catch (error) {
            console.error('Failed to initialize WebSocket:', error);
            setConnectionStatus('Failed to Connect');
        }
    };

    // Initialize on component mount and roomId change
    useEffect(() => {
        if (roomId && current_userId) {
            // Fetch initial history
            fetchHistory();

            // Initialize WebSocket
            initializeWebSocket();
        }

        // Cleanup on unmount or roomId change
        return () => {
            if (ws.current) {
                ws.current.close();
            }
        };
    }, [roomId, current_userId]);

    // Send message via WebSocket
    const sendMessage = async () => {
        const content = newMessage.trim();
        if (!content) {
            console.log("empty message, can't send");
            return;
        }

        if (!isConnected || !ws.current) {
            console.log("WebSocket not connected, falling back to API");
            // Fallback to API if WebSocket is not connected
            await sendMessageViaAPI(content);
            return;
        }

        try {
            const messageData = {
                type: 'send_message',
                roomId: roomId,
                senderId: current_userId,
                recipientId: null, // Will be determined by the server based on roomId
                content: content,
                timestamp: new Date().toISOString()
            };

            console.log("Sending WebSocket message:", messageData);
            ws.current.send(JSON.stringify(messageData));
            setNewMessage('');

        } catch (error) {
            console.log("Error sending WebSocket message:", error);
            // Fallback to API
            await sendMessageViaAPI(content);
        }
    };

    // Fallback API method
    const sendMessageViaAPI = async (content) => {
        try {
            console.log("Sending message via API:", {
                roomId,
                senderId: current_userId,
                recipientId: null,
                content
            });

            const response = await messageAPI.sendMessage(current_userId, null, roomId, content);
            if (response) {
                console.log("API response: ", response);
                setNewMessage('');
                // Refresh history to get the new message
                await fetchHistory();
            }
        } catch (error) {
            console.log("Error sending message via API:", error);
        }
    };

    // Handle form submission
    const handleSendMessage = (e) => {
        e.preventDefault();
        sendMessage();
    };

    // Handle Enter key press
    const handleKeyDown = (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    };

    return (
        <div className="chat-container">
            {/* Connection Status */}
            <div className="connection-status" style={{
                padding: '8px 12px',
                backgroundColor: isConnected ? '#28a745' : '#dc3545',
                color: 'white',
                fontSize: '12px',
                borderRadius: '4px',
                marginBottom: '10px',
                textAlign: 'center'
            }}>
                {connectionStatus}
            </div>

            {/* Chat Header */}
            <div className="chat-header" style={{
                padding: '10px 0',
                borderBottom: '1px solid #333',
                marginBottom: '15px'
            }}>
                <h3 style={{ margin: 0, color: '#fff' }}>
                    Chat with {friendUsername}
                </h3>
            </div>

            {/* Messages Container */}
            <div className="messages-container" style={{
                height: '400px',
                overflowY: 'auto',
                marginBottom: '20px',
                padding: '10px',
                backgroundColor: '#1a1a1a',
                borderRadius: '8px'
            }}>
                {history.length === 0 ? (
                    <div style={{ textAlign: 'center', color: '#888', marginTop: '20px' }}>
                        No messages yet. Start the conversation!
                    </div>
                ) : (
                    history.map((message) => {
                        const isMyMessage = message.senderId == current_userId;
                        return (
                            <div
                                key={message.messageId}
                                className={`message ${isMyMessage ? 'my-message' : 'other-message'}`}
                                style={{
                                    marginBottom: '15px',
                                    display: 'flex',
                                    flexDirection: 'column',
                                    alignItems: isMyMessage ? 'flex-end' : 'flex-start'
                                }}
                            >
                                <div
                                    className="message-bubble"
                                    style={{
                                        maxWidth: '70%',
                                        padding: '10px 15px',
                                        borderRadius: '18px',
                                        backgroundColor: isMyMessage ? '#646cff' : '#333',
                                        color: '#fff',
                                        wordWrap: 'break-word'
                                    }}
                                >
                                    <div className="message-header" style={{
                                        fontSize: '12px',
                                        marginBottom: '5px',
                                        opacity: 0.8
                                    }}>
                                        <strong>
                                            {isMyMessage
                                                ? 'You'
                                                : (message.senderUsername || friendUsername || `User ${message.senderId}`)}
                                        </strong>
                                        {message.timestamp && (
                                            <span className="message-time" style={{ marginLeft: '8px' }}>
                                                {new Date(message.timestamp).toLocaleTimeString([], {
                                                    hour: '2-digit',
                                                    minute: '2-digit'
                                                })}
                                            </span>
                                        )}
                                    </div>
                                    <div className="message-content">
                                        {message.content}
                                    </div>
                                </div>
                            </div>
                        );
                    })
                )}
                <div ref={messagesEndRef} />
            </div>

            {/* Message Input */}
            <div className="keyboard-input-container">
                <form onSubmit={handleSendMessage} style={{
                    display: 'flex',
                    gap: '10px',
                    alignItems: 'flex-end'
                }}>
                    <textarea
                        value={newMessage}
                        placeholder="Type your message here..."
                        onChange={(e) => setNewMessage(e.target.value)}
                        onKeyDown={handleKeyDown}
                        style={{
                            flex: 1,
                            minHeight: '40px',
                            maxHeight: '120px',
                            padding: '10px',
                            borderRadius: '8px',
                            border: '1px solid #333',
                            backgroundColor: '#2a2a2a',
                            color: '#fff',
                            fontSize: '14px',
                            resize: 'vertical'
                        }}
                        rows={1}
                    />
                    <button
                        type="submit"
                        className="send-button"
                        disabled={!newMessage.trim() || !isConnected}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: (!newMessage.trim() || !isConnected) ? '#555' : '#646cff',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '8px',
                            cursor: (!newMessage.trim() || !isConnected) ? 'not-allowed' : 'pointer',
                            fontSize: '14px'
                        }}
                    >
                        Send
                    </button>
                </form>
                <div style={{
                    fontSize: '12px',
                    marginTop: '5px',
                    color: '#888',
                    textAlign: 'center'
                }}>
                    Press Enter to send • Shift+Enter for new line
                </div>
            </div>
        </div>
    );
}

export default ChatRoom;
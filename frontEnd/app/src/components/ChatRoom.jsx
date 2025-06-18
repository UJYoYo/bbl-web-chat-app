import { useEffect, useState } from 'react';
import { messageAPI } from './Api.jsx';
function ChatRoom({ roomId, friendUsername }) {
    const [history, setHistory] = useState([]);
    const [newMessage, setNewMessage] = useState('');

    const current_username = localStorage.getItem("username");
    const current_userId = localStorage.getItem("userId");

    console.log("chatroom room ID:", roomId);
    console.log("friendusername:", friendUsername);

    let tmp_history;
    const fetchHistory = async () => {
        try {
            tmp_history = await messageAPI.getHistory(1);
            if (tmp_history) {
                console.log("tmp-history", tmp_history);
                setHistory(tmp_history);
            }
        } catch (e) {
            console.log("error chat room: ", e);
        }
    }
    useEffect(() => {
        fetchHistory();
    }, [roomId]);

    const handleSendMessage = (newMessage) => {
        
    }
    return (
        < div className="chat-container">
            {history.map((message) => {
                const isMyMessage = message.senderId == 3;
                return (
                    <div
                        key={message.messageId}
                        className={`message ${isMyMessage ? 'my-message' : 'other-message'}`}
                    >
                        <div className="message-header">
                            <strong>
                                {isMyMessage
                                    ? ('You' || current_username)
                                    : (friendUsername || message.senderId)}
                            </strong>
                            <span className="message-time">
                                {/* You can add timestamp here if available */}
                            </span>
                        </div>
                        <div className="message-content">
                            {message.content}
                        </div>
                    </div>
                );
            })}
            < div className="keyboard-input-container">
                <textarea
                    value={newMessage}
                    placeholder='type here'
                    onChange={(e) => setNewMessage(e.target.value)}
                    onKeyChanged={handleSendMessage(newMessage)}
                >
                </textarea>
            </div>
        </div >
    );
}

export default ChatRoom;
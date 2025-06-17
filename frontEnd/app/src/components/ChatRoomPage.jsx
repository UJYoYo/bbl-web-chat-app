// import { useState } from 'react';
// // import { ArrowLeft, Send } from 'lucide-react';

// function ChatRoomPage({ username = "Salapao", onBack }) {
//     const [message, setMessage] = useState('');
//     const [messages, setMessages] = useState([
//         { id: 1, text: "Hey! How are you?", sender: username, timestamp: "10:30 AM" },
//         { id: 2, text: "I'm doing great! How about you?", sender: "me", timestamp: "10:32 AM" },
//         { id: 3, text: "Pretty good! Want to hang out later?", sender: username, timestamp: "10:35 AM" },
//         { id: 4, text: "Sure! What time works for you?", sender: "me", timestamp: "10:36 AM" },
//     ]);

//     const handleSendMessage = (e) => {
//         if (e) e.preventDefault();
//         if (message.trim()) {
//             const newMessage = {
//                 id: messages.length + 1,
//                 text: message,
//                 sender: "me",
//                 timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
//             };
//             setMessages([...messages, newMessage]);
//             setMessage('');
//         }
//     };

//     const handleBack = () => {
//         if (onBack) onBack();
//     };

//     return (
//         <div style={{
//             display: 'flex',
//             flexDirection: 'column',
//             height: '100vh',
//             backgroundColor: '#f5f5f5',
//             color: '#333'
//         }}>
//             {/* Header */}
//             <header style={{
//                 display: 'flex',
//                 alignItems: 'center',
//                 padding: '12px 16px',
//                 backgroundColor: '#ffffff',
//                 borderBottom: '1px solid #e0e0e0',
//                 position: 'sticky',
//                 top: 0,
//                 zIndex: 10
//             }}>
//                 <button
//                     onClick={handleBack}
//                     style={{
//                         background: 'none',
//                         border: 'none',
//                         padding: '8px',
//                         marginRight: '12px',
//                         cursor: 'pointer',
//                         color: '#646cff',
//                         display: 'flex',
//                         alignItems: 'center'
//                     }}
//                 >
//                     {/* <ArrowLeft size={24} /> */}
//                 </button>
//                 <div>
//                     <h2 style={{
//                         margin: 0,
//                         fontSize: '18px',
//                         fontWeight: '600',
//                         color: '#333'
//                     }}>{username}</h2>
//                     <span style={{
//                         fontSize: '12px',
//                         color: '#28a745'
//                     }}>Online</span>
//                 </div>
//             </header>

//             {/* Messages Area */}
//             <div style={{
//                 flex: 1,
//                 overflowY: 'auto',
//                 padding: '16px',
//                 display: 'flex',
//                 flexDirection: 'column'
//             }}>
//                 {messages.map((msg) => (
//                     <div
//                         key={msg.id}
//                         style={{
//                             display: 'flex',
//                             justifyContent: msg.sender === 'me' ? 'flex-end' : 'flex-start',
//                             marginBottom: '12px'
//                         }}
//                     >
//                         <div style={{
//                             maxWidth: '70%',
//                             padding: '12px 16px',
//                             borderRadius: '18px',
//                             backgroundColor: msg.sender === 'me' ? '#646cff' : '#ffffff',
//                             color: msg.sender === 'me' ? '#ffffff' : '#333',
//                             boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
//                             position: 'relative'
//                         }}>
//                             <p style={{ margin: 0, marginBottom: '4px' }}>{msg.text}</p>
//                             <span style={{
//                                 fontSize: '11px',
//                                 opacity: 0.7,
//                                 display: 'block',
//                                 textAlign: 'right'
//                             }}>{msg.timestamp}</span>
//                         </div>
//                     </div>
//                 ))}
//             </div>

//             {/* Input Area */}
//             <div style={{
//                 padding: '12px 16px',
//                 backgroundColor: '#ffffff',
//                 borderTop: '1px solid #e0e0e0'
//             }}>
//                 <div style={{
//                     display: 'flex',
//                     alignItems: 'center',
//                     gap: '12px',
//                     backgroundColor: '#f8f9fa',
//                     borderRadius: '24px',
//                     padding: '8px 16px'
//                 }}>
//                     <input
//                         type="text"
//                         value={message}
//                         onChange={(e) => setMessage(e.target.value)}
//                         onKeyPress={(e) => e.key === 'Enter' && handleSendMessage(e)}
//                         placeholder="Type a message..."
//                         style={{
//                             flex: 1,
//                             border: 'none',
//                             outline: 'none',
//                             backgroundColor: 'transparent',
//                             fontSize: '16px',
//                             color: '#333'
//                         }}
//                     />
//                     <button
//                         onClick={handleSendMessage}
//                         disabled={!message.trim()}
//                         style={{
//                             background: message.trim() ? '#646cff' : '#ccc',
//                             border: 'none',
//                             borderRadius: '50%',
//                             width: '40px',
//                             height: '40px',
//                             display: 'flex',
//                             alignItems: 'center',
//                             justifyContent: 'center',
//                             cursor: message.trim() ? 'pointer' : 'not-allowed',
//                             color: '#ffffff'
//                         }}
//                     >
//                         {/* <Send size={20} /> */}
//                     </button>
//                 </div>
//             </div>
//         </div>
//     );
// }

// export default ChatRoomPage;










// /*
// this page = functions and style of chat page only. no navigation?

// */

// import { useNavigate } from 'react-router-dom';
// import '../styles/ChatPage.css'

// function ResponsiveChatPage() {
//     console.log('🔍 ChatRoom component rendering...');

//     const navigate = useNavigate();

//     const ChatRooms = [
//         { id: 1, username: 'Salapao' },
//         { id: 2, username: 'Bao' },
//         { id: 3, username: 'Pao' }
//     ]

//     const selectRoom = (room) => {
//         // navigate('/main/chats/${room.id}', { state: { username: room.username } });
//         navigate(`/main/chats/${room.id}`, { state: { username: room.username } });

//     }
//     return (
//         <div className="chatList">
//             <p>You have {ChatRooms.length} friends</p>
//             <div className="roomList">
//                 {ChatRooms.map((room) =>
//                     <div className="room"
//                         key={room.id}
//                         onClick={() => selectRoom(room)}
//                     >{room.username}</div>
//                 )}
//             </div>

//         </div>
//     )

// }

// export default ResponsiveChatPage;
/*
this page = functions and style of chat page only. no navigation?
nope, we'll combine both the list and chat room into one since the style will be different.
*/

import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ChatRoom from './ChatRoom.jsx';
import '../styles/ChatPage.css'

function ResponsiveChatPage() {
    const { roomId } = useParams();
    const [selectedRoom, setSelectedRoom] = useState(roomId || null);
    const [isMobile, setIsMobile] = useState(window.innerWidth < 700);
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const [messages, setMessages] = useState({
        'Salapao': [
            { id: 1, text: "Hey! How are you?", sender: "Salapao", timestamp: "10:30 AM" },
            { id: 2, text: "I'm doing great! How about you?", sender: "me", timestamp: "10:32 AM" },
            { id: 3, text: "Pretty good! Want to hang out later?", sender: "Salapao", timestamp: "10:35 AM" },
            { id: 4, text: "Sure! What time works for you?", sender: "me", timestamp: "10:36 AM" },
        ],
        'Bao': [
            { id: 1, text: "What's up!", sender: "Bao", timestamp: "9:15 AM" },
            { id: 2, text: "Not much, just working. You?", sender: "me", timestamp: "9:20 AM" },
        ],
        'Pao': [
            { id: 1, text: "Did you see the game last night?", sender: "Pao", timestamp: "8:45 AM" },
        ]
    });

    const ChatRooms = [
        { id: 1, roomId: 'Salapao', lastMessage: 'Sure! What time works for you?', time: '10:36 AM' },
        { id: 2, roomId: 'Bao', lastMessage: 'Not much, just working. You?', time: '9:20 AM' },
        { id: 3, roomId: 'Pao', lastMessage: 'Did you see the game last night?', time: '8:45 AM' }
    ];

    console.log('Current URL:', window.location.pathname);
    console.log('roomId from useParams():', roomId);
    console.log('Type of roomId:', typeof roomId);

    useEffect(() => {
        const handleResize = () => {
            setIsMobile(window.innerWidth < 700);
        };
        window.addEventListener('resize', handleResize);
        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []); //[] why brackets

    const selectRoom = (roomId) => {
        setSelectedRoom(roomId);
        navigate(`/main/chats/${roomId}`);
    }

    // const historyMessages = selectedRoom ? messages[selectedRoom] || [] : [];

    if (isMobile) {
        if (selectedRoom) {
            return (
                <div className="mobile-chat-room">
                    <p>{selectedRoom}</p>
                </div>
            );
        }
        else {
            return (
                <div className="mobile-chat-list">
                    <p>You have {ChatRooms.length} friends</p>
                    <div className="roomList">
                        {ChatRooms.map((room) =>
                            <div className="room"
                                key={room.id}
                                onClick={() => selectRoom(room.roomId)}
                            >{room.roomId}</div>
                        )}
                    </div>

                </div >
            )
        }
    }
    return (
        <div className="desktop-container">
            <div className="desktop-list">
                <p>You have {ChatRooms.length} friends</p>
                {ChatRooms.map((room) =>
                    <div className="room"
                        key={room.id}
                        onClick={() => selectRoom(room.roomId)}
                    >{room.roomId}</div>
                )}
            </div>
            {selectedRoom &&
                <ChatRoom roomId={roomId} />
            }
            {selectedRoom == null && (
                <div className="desktop-no-chat-selected">
                    <h3>Select a chat to start messaging</h3>
                    <p>Choose from your conversations on the left</p>
                </div>
            )}
        </div>
    );

};

export default ResponsiveChatPage;
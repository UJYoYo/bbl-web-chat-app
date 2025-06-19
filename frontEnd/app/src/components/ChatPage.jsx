/*
this page = functions and style of chat page only. no navigation?
nope, we'll combine both the list and chat room into one since the style will be different.
*/

import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { friendsAPI } from './Api.jsx';
import ChatRoom from './ChatRoom.jsx';
import '../styles/ChatPage.css'

function ResponsiveChatPage() {
    const { roomId } = useParams();
    const [selectedRoom, setSelectedRoom] = useState('' || null);
    const [isMobile, setIsMobile] = useState(window.innerWidth < 700);
    const [message, setMessage] = useState('');
    const [chatRooms, setChatRooms] = useState([]);
    const current_username = localStorage.getItem("username");

    const navigate = useNavigate();

    console.log('Current URL:', window.location.pathname);
    console.log('roomId from useParams():', roomId);
    console.log('Type of roomId:', typeof roomId);
    console.log(chatRooms);

    const loadFriends = async () => {
        try {
            const friendsList = await friendsAPI.getFriends(current_username);
            console.log("friend", friendsList);
            setChatRooms(friendsList || []);
        } catch (e) {
            console.log("Error: ", e);
        }

    }
    useEffect(() => {
        loadFriends();
        const handleResize = () => {
            setIsMobile(window.innerWidth < 700);
        };
        window.addEventListener('resize', handleResize);
        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []); //[] why bracket

    const selectRoom = (roomId, username) => {
        setSelectedRoom(username);
        navigate(`/main/chats/${roomId}`);
    }

    useEffect(() => {
        console.log("select", selectedRoom);
    }, [selectedRoom]);

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
                    <p>You have {chatRooms.length} friends</p>
                    <div className="roomList">
                        {chatRooms.map((room) =>
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
                <p>You have {chatRooms.length} friends</p>
                {chatRooms.map((room) =>
                    <div className="room"
                        key={room.roomId}
                        onClick={() => selectRoom(room.roomId, room.username)}
                    >{room.username}</div>
                )}
            </div>
            {selectedRoom && (
                <div className="desktop-chat-selected">
                    <ChatRoom roomId={roomId} friendUsername={selectedRoom} />
                </div>
            )}
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
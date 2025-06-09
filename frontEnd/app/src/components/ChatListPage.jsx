/*
this page = functions and style of chat page only. no navigation?

*/

import { useNavigate } from 'react-router-dom';
import '../styles/ChatListPage.css'

function ChatListPage() {
    const navigate = useNavigate();

    const ChatRooms = [
        { id: 1, username: 'Salapao' },
        { id: 2, username: 'Bao' },
        { id: 3, username: 'Pao' }
    ]

    const selectRoom = (room) => {
        // navigate('/main/chats/${room.id}', { state: { username: room.username } });
        navigate(`/main/chats/${room.id}`, { state: { username: room.username } });

    }
    return (
        <div className="chatList">
            <p>You have {ChatRooms.length} friends</p>
            <div className="roomList">
                {ChatRooms.map((room) =>
                    <div className="room"
                        key={room.id}
                        onClick={() => selectRoom(room)}
                    >{room.username}</div>
                )}
            </div>

        </div>
    )

}

export default ChatListPage;
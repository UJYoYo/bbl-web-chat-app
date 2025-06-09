/*
this page = functions and style of chat page only. no navigation?

*/

import { useState } from 'react';
import '../styles/ChatPage.css'

function ChatPage() {
    const [username, selectUsername] = useState('');

    const handleSelectUser = (e) => {
        e.preventDefault();
        console.log(e);
    }
    const ChatRooms = [
        { id: 1, username: 'Salapao' },
        { id: 2, username: 'Bao' },
        { id: 3, username: 'Pao' }
    ]
    return (
        <div className="chatList">
            {/* <div className="title"> */}
            <p>You have {ChatRooms.length} friends</p>
            {/* </div> */}
            <div className="roomList">
                {ChatRooms.map((room) =>
                    <div className="room"
                        key={room.id}
                        onClick={() => console.log(room.username)}
                    >{room.username}</div>
                )}
            </div>

        </div>
    )

}

export default ChatPage;
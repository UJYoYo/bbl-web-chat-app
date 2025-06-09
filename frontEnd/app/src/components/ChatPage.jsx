/*
this page = functions and style of chat page only. no navigation?

*/

import { useState } from 'react';

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
        <div>
            <div>
                <h2>Messages</h2>
                <p>Total friends: {ChatRooms.length}</p>
            </div>
            <div>
                {ChatRooms.map((room) =>
                    <div
                        key={room.id}
                        onClick={(e) => selectUsername(room.id)}
                    >{room.username}</div>
                )}
            </div>

        </div>
    )

}

export default ChatPage;
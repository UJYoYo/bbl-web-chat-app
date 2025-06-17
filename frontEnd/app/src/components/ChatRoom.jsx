import { useState } from 'react';

function ChatRoom({ roomId }) {
    console.log(roomId);
    return (
        < div >
            <p>{roomId}</p>
        </div >
    );
}

export default ChatRoom;
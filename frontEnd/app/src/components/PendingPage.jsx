import { useState } from 'react'
import '../styles/PendingPage.css'

function PendingPage() {
    //variable store pending requests
    const [pendingRequests, setPendingRequests] = useState([
        {
            id: 1,
            username: 'shane',
        },
        {
            id: 2,
            username: 'non',
        },
        {
            id: 3,
            username: 'bp',
        }
    ]);

    const handleAcceptRequest = (requestId) => {
        const newList = pendingRequests.filter(req => req.id !== requestId);
        setPendingRequests(newList);
        console.log("Added user");

        //show notification
    }

    const handleDeclineRequest = (requestId) => {
        const newList = pendingRequests.filter(req => req.id !== requestId);
        setPendingRequests(newList);
        console.log("Declined user");
    }

    return (
        <div>
            <p>Your {pendingRequests.length} friends(?) are waiting for your acceptance</p>
            <div>
                {pendingRequests.length > 0 &&
                    (
                        <div className='request-container'>
                            {pendingRequests.map((request) => (
                                <div key={request.id} className="row-container">
                                    <div className="username">
                                        {request.username}
                                    </div>
                                    <div className="button-container">
                                        <button
                                            className="accept-button"
                                            onClick={() => handleAcceptRequest(request.id)}
                                        >Accept
                                        </button>
                                        <button
                                            className="decline-button"
                                            onClick={() => handleDeclineRequest(request.id)}
                                        >Decline
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )
                }
            </div>
            <div>
            </div>
        </div >
    )
}

export default PendingPage;
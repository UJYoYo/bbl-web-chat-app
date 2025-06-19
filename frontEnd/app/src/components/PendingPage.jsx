import { useEffect, useState } from 'react'
import { friendsAPI } from './Api.jsx';
import '../styles/PendingPage.css'

function PendingPage() {
    //variable store pending requests
    const [pendingRequests, setPendingRequests] = useState([]);
    const current_username = localStorage.getItem("username");
    const current_userId = localStorage.getItem("userId");
    console.log(current_userId);
    useEffect(() => {
        const fetchData = async () => {
            try {
                const pendingQuery = await friendsAPI.getPendingRequests(current_username);
                if (pendingQuery) {
                    setPendingRequests(pendingQuery);
                    console.log("pending query", pendingQuery);
                }
            } catch (e) {
                console.log("error pending query:", e);
                setPendingRequests([]);
            }
        };
        fetchData();
    }, []);
    useEffect(() => {
        console.log("pending request state updated:", pendingRequests);
    }, [pendingRequests]);


    const handleAcceptRequest = async (senderId) => {

        try {
            const updateState = await friendsAPI.sendRequestStatus(senderId, current_userId, "accepted");
            if (updateState) {
                const newList = pendingRequests.filter(req => req.id !== senderId);
                setPendingRequests(newList);
                console.log("Added user");
            }
        } catch (e) {
            console.log("error pending page: ", e);
        }
        //show notification
    }

    const handleDeclineRequest = async (senderId) => {

        try {
            const updateState = await friendsAPI.sendRequestStatus(senderId, current_userId, "rejected");
            if (updateState) {
                const newList = pendingRequests.filter(req => req.id !== senderId);
                setPendingRequests(newList);
                console.log("Declined user");
            }
        } catch (e) {
            console.log("error pending page: ", e);
        }
    }

    return (
        <div>
            <p>Your {pendingRequests.length} friends(?) are waiting for your acceptance</p>
            <div>
                {pendingRequests.length > 0 &&
                    (
                        <div className='request-container'>
                            {pendingRequests.map((request) => (
                                <div key={request.userId} className="row-container">
                                    <div className="username">
                                        {request.username}
                                    </div>
                                    <div className="button-container">
                                        <button
                                            className="accept-button"
                                            onClick={() => handleAcceptRequest(request.userId)}
                                        >Accept
                                        </button>
                                        <button
                                            className="decline-button"
                                            onClick={() => handleDeclineRequest(request.userId)}
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
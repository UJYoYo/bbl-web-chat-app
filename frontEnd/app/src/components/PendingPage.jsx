import { useEffect, useState } from 'react'
import { friendsAPI } from './Api.jsx';
import '../styles/PendingPage.css'
import RefreshIcon from '../assets/refresh-icon.svg'

function PendingPage() {
    //variable store pending requests
    const [pendingRequests, setPendingRequests] = useState([]);
    const current_username = localStorage.getItem("username");
    const current_userId = localStorage.getItem("userId");
    console.log(current_userId);

    const getPendingRequest = () => {
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
    }

    useEffect(() => {
        getPendingRequest();
    }, []);


    useEffect(() => {
        console.log("pending request state updated:", pendingRequests);
    }, [pendingRequests]);


    const handleAcceptRequest = async (senderId) => {

        try {
            const updateState = await friendsAPI.sendRequestStatus(senderId, current_userId, "accepted");
            if (updateState) {
                const newList = pendingRequests.filter(req => req.userId !== senderId);
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
                const newList = pendingRequests.filter(req => req.userId !== senderId);
                setPendingRequests(newList);
                console.log("Declined user");
            }
        } catch (e) {
            console.log("error pending page: ", e);
        }
    }

    return (
        <div>
            <div className="bar-container">
                <p>Can't believe {pendingRequests.length} soul(s) want to be your friends</p>
                <button className="refresh-button" onClick={getPendingRequest}>
                    <img className="refresh-icon" src={RefreshIcon} alt="Refresh"></img>
                </button>
            </div>
            <hr className="solid"></hr>
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
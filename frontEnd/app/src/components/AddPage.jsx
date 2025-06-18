import { useState, useEffect } from 'react';
import { friendsAPI } from './Api.jsx';
import "../styles/AddPage.css"

function AddPage() {
    const [searched, setSearched] = useState(false);
    const [searchQuery, setSearchQuery] = useState('');
    const [resultList, setResultList] = useState([]);
    const [requestList, setRequestList] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    //toast notification
    const [showNotif, setShowNotif] = useState(false);
    const current_username = localStorage.getItem("username");

    //handle show notification after request is sent
    const handleShowNotif = () => {
        setShowNotif(true);

        setTimeout(() => {
            setShowNotif(false);
        }, 1000)
    }
    //handle search = recieve search input and get from database
    const handleSearch = async (e) => {
        e.preventDefault();
        const tmp_searchQuery = searchQuery.trim();
        // console.log(tmp_searchQuery);
        if (tmp_searchQuery.length === 0)
            return;
        setSearched(true);
        setIsLoading(true);

        try {
            const userList = await friendsAPI.getUsers(tmp_searchQuery);
            let userArray;
            if (Array.isArray(userList)) {
                userArray = userList;
            } else if (userList && typeof userList === 'object') {
                userArray = [userList];  // Wrap single object in array
            } else {
                userArray = [];
            }
            setResultList(userArray);
        } catch (e) {
            console.log("error: ", e);
            setResultList([]);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        console.log('🔄 ResultList updated:', resultList);
        console.log('🔄 ResultList length:', resultList.length);
    }, [resultList]);


    const handleRequestList = async (recipientUser) => {
        try {
            const request = await friendsAPI.sendRequests(current_username, recipientUser.username);
            console.log(request);
            if (request) {
                setRequestList([...requestList, recipientUser.id]);
                console.log(`Sending friend request to ${requestList}`);
            }
        } catch (e) {
            console.log("Error request list", e);
        }
        console.log("requestlist", requestList);
        handleCloseModal();
        handleShowNotif();
    }

    const handleInputChange = (e) => {
        setSearchQuery(e.target.value);
        setSearched(false);
        setResultList([]);
    }

    const handleShowModal = (user) => {
        setSelectedUser(user)
        setShowModal(true);
    }
    const handleCloseModal = () => {
        setSelectedUser(null);
        setShowModal(false);
    }

    return (
        <div>
            <div className="search-container">
                <p>Damn, you have friends?</p>
                <form onSubmit={handleSearch}>
                    <input
                        type="text"
                        value={searchQuery}
                        onChange={handleInputChange}
                        placeholder="Search your friend's username"
                    />
                    <button type="submit">Search</button>
                </form>
            </div>
            <div className="result-container">
                {isLoading && (
                    <div>
                        <p>Searching....</p>
                    </div>
                )}
                {!isLoading && searched && resultList.length === 0 && (
                    <div>
                        <p>No user found in the database. Please try again.</p>
                    </div>
                )}
                {searched && resultList.length > 0 && (resultList.map((user) => (
                    <div
                        className={`friendList ${requestList.includes(user.id) ? 'disabled' : ''}`}
                        key={user.userId}
                        onClick={requestList.includes(user.id) ? undefined : () => handleShowModal(user)}
                        style={{
                            cursor: requestList.includes(user.id) ? 'not-allowed' : 'pointer',
                            opacity: requestList.includes(user.id) ? 0.7 : 1
                        }}
                    >{user.username}
                        {requestList.includes(user.id) && showNotif && (
                            <div className="notification-container">
                                {`Request sent to ${user.username}!`}
                            </div>
                        )}
                    </div>
                )))}
            </div>

            {showModal && selectedUser && (
                < div className="modal-container">
                    {/* {console.log(user.username)} */}
                    <p>You want to add this person?</p>
                    <div className="button-container">
                        <button
                            onClick={() => handleRequestList(selectedUser)}
                        >Add</button>
                        <button
                            onClick={() => handleCloseModal()}
                        >Cancel</button>
                    </div>
                </div>
            )
            }

            {/* {{ showModal }} */}
        </div >
        //form




    )
}

export default AddPage
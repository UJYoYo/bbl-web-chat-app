import { useState, useEffect } from 'react';
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

    const mockUsers = [
        { id: 1, username: 'Salapao' },
        { id: 2, username: 'Bp' },
        { id: 3, username: 'Shane' },
        { id: 4, username: 'Baobao' },
        { id: 5, username: 'Krungnumpao', },
        { id: 6, username: 'Baobaochicken' }
    ];
    //handle show notification after request is sent
    const handleShowNotif = () => {
        setShowNotif(true);

        setTimeout(() => {
            setShowNotif(false);
        }, 1000)
    }
    //handle search = recieve search input and get from database
    const handleSearch = (e) => {
        e.preventDefault();
        console.log(searchQuery.trim());
        if (searchQuery.trim().length === 0)
            return;
        setSearched(true);
        setIsLoading(true);

        setTimeout(() => {
            const results = mockUsers.filter(user => user.username.toLowerCase().includes(searchQuery.toLowerCase()))
            setResultList(results);
            setIsLoading(false);
        }, 2000);

    };

    const handleRequestList = (user) => {
        setRequestList([...requestList, user.id]);
        console.log(`Sending friend request to ${user.username}`);

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
                        key={user.id}
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
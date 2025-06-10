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

    const mockUsers = [
        { id: 1, username: 'salapao' },
        { id: 2, username: 'bp' },
        { id: 3, username: 'shane' },
        { id: 4, username: 'baobao' },
        { id: 5, username: 'krungnumpao', },
        { id: 6, username: 'baobaochicken' }
    ];
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
        }, 1000);

    };

    const handleRequestList = (user) => {
        setRequestList([...requestList, user.id]);
        console.log(`Sending friend request to ${user.username}`);

        handleCloseModal();
    }

    const handleInputChange = (e) => {
        setSearchQuery(e.target.value);
        setSearched(false);
        setResultList('');
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
                <p>Find your friends...if you have them</p>
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
                    <div className="friendList"
                        key={user.id}
                        onClick={() => handleShowModal(user)}
                    >{user.username}
                        {requestList.includes(user.id) && console.log(user.id)}
                    </div>
                )))}
            </div>

            {showModal && selectedUser && (
                <div className="modal-container" style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    backgroundColor: 'black',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                }}>
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
            )}
        </div >
        //form




    )
}

export default AddPage
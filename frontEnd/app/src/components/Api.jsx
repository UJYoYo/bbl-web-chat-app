
const API_CONFIG = {
    baseURL: "http://34.87.125.159",
    headers: {
        'Content-Type': 'application/json',
    }
};

const apiCall = async (endpoint, option = {}) => {
    const url = `${API_CONFIG.baseURL}${endpoint}`;

    const config = {
        headers: API_CONFIG.headers,
        ...option,
    };
    try {
        const response = await fetch(url, config);
        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || `HTTP error! status: ${response.status}`);
        }

        return data;
    } catch (error) {
        console.error('API call error:', error);
        throw error;
    }
};

export const userAPI = {
    // Register user
    register: async (username, password) => {
        return apiCall('/api/register', {
            method: 'POST',
            body: JSON.stringify({
                username: username.trim(),
                password: password
            })
        });
    },
};

export const friendsAPI = {
    getFriends: async (username) => {
        return apiCall(`/api/friends/getFriends?username=${username}`, {
            method: 'GET',
        });
    },
    getUsers: async (username) => {
        return apiCall(`/api/friends/getUser?username=${username}`, {
            method: 'GET',
        });
    },
    sendRequests: async (senderUsername, recipientUsername) => {
        return apiCall(`/api/friends/sendRequest`, {
            method: 'POST',
            body: JSON.stringify({
                senderUsername: senderUsername.trim(),
                recipientUsername: recipientUsername.trim(),
            })
        });
    },
    getPendingRequests: async (username) => {
        return apiCall(`/api/friends/getPendingRequests?username=${username}`, {
            method: 'GET',
        });
    },
    sendRequestStatus: async (senderId, recipientId, requestStatus) => {
        return apiCall(`/api/friends/requests`, {
            method: 'POST',
            body: JSON.stringify({
                senderId: senderId,
                recipientId: recipientId,
                status: requestStatus
            })
        })
    },
};

export const messageAPI = {
    getHistory: async (roomId) => {
        return apiCall(`/api/chat/getHistory?roomId=${roomId}`, {
            method: 'GET',
        });
    },
    sendMessage: async (senderId, recipientId, roomId, content) => {
        return apiCall(`/api/chat.send`, {
            method: 'POST',
            body: JSON.stringify({
                senderId: senderId,
                reciepientId: recipientId,
                content: content,
                roomId: roomId,
            }),
        });
    },
}
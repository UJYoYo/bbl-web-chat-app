import { useState } from 'react';
import { Outlet, Navlink } from 'react-router-dom';
import '../styles/ChatPage.css';

function ChatLayout() {
  const [activeTab, setActiveTab] = useState('Chat');
  const tabs = ['Chat', 'Add', 'Pending'];
  const chatRoom = [
    { id: 1, name: 'Baipor' },
    { id: 2, name: 'Copter' },
    { id: 3, name: 'Non' },
    { id: 4, name: 'Prajogo' },
  ]

  const renderPages = () => {
    switch (activeTab) {
      case 'Chat':
        return (<h1></h1>)
      case 'Add':
        return (<h1>add page</h1>)
      case 'Pending':
        return (<h1>pending page</h1>)
      default:
        return null;
    }
  }
  // console.log(chatRoom[0].name);

   //test endpoint
   const fetchHelloEndPoint = async () => {
    try {
        const response = await fetch('http://localhost:1234/hello');

        if (!response.ok) {
            console.log("dead", response.status);
        }
        const data = await response.text();
        console.log(data);
    }
    catch (error) {
        console.error('error');
    }
}
useEffect(() => {
    fetchHelloEndPoint();
}, []);
  return (
    <>
      {tabs.map((tab) =>
        <button
          key={tab}
          onClick={() => setActiveTab(tab)}
        >
          {tab}
        </button>
      )}

      <div>
        {renderPages()}
      </div>
    </>
  );
}

export default ChatLayout;

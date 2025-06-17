import { useState, useEffect } from 'react';
import { Outlet, NavLink, useLocation } from 'react-router-dom';
import '../styles/MainLayout.css';

function MainLayout() {
  const [isMobile, setIsMobile] = useState(window.innerWidth < 700);
  const location = useLocation();

  const chatOnMobile = isMobile && location.pathname.includes('/main/chats');
  // console.log(isMobile);
  // console.log(location.pathname);
  useEffect(() => {
    const handleResize = () => {
      // console.log(chatOnMobile);
      console.log(window.innerWidth);
      setIsMobile(window.innerWidth < 700);
    };

    window.addEventListener('resize', handleResize);
    return () => {
      window.removeEventListener('resize', handleResize);
    };
  });

  return (
    <div className="chat-layout">
      {!chatOnMobile && (
        <header className="chat-header">
          <h1>BALAPAO </h1>
          <nav>
            <NavLink
              to='/main/chats'
              className={({ isActive }) => `nav-button ${isActive ? 'active' : ''}`}
            >Chat</NavLink>
            <NavLink
              to='/main/add'
              className={({ isActive }) => `nav-button ${isActive ? 'active' : ''}`}
            >Add Friends</NavLink>
            <NavLink
              to='/main/pending'
              className={({ isActive }) => `nav-button ${isActive ? 'active' : ''}`}
            >Pending</NavLink>
          </nav>
        </header>
      )}
      <main className={chatOnMobile ? 'fullscreen' : ''}>
        <Outlet />
      </main>
    </div>
  )
}

export default MainLayout;

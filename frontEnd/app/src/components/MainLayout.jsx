import { useState } from 'react';
import { Outlet, NavLink } from 'react-router-dom';
import '../styles/MainLayout.css';

function MainLayout() {
  return (
    <div className="chat-layout">
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
      <main>
        <Outlet />
      </main>
    </div>
  )
}

export default MainLayout;

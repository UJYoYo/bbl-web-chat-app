import { Routes, Route, Navigate } from 'react-router-dom'
import '../styles/index.css'
import Register from './Register.jsx'
import MainLayout from './MainLayout.jsx'
import ResponsiveChatPage from './ChatPage.jsx'
// import ChatRoomPage from './ChatRoomPage.jsx'
import AddPage from './AddPage.jsx'
import PendingPage from './PendingPage.jsx'

function App() {
  return (
    <Routes>
      <Route path='/' element={<Register />} />
      <Route path='/main' element={<MainLayout />}>
        <Route index element={<Navigate to="/main/chats" replace />} />
        <Route path='chats' element={<ResponsiveChatPage />} />
        <Route path='chats/:roomId' element={<ResponsiveChatPage />} />
        <Route path='add' element={<AddPage />} />
        <Route path='pending' element={<PendingPage />} />
      </Route>
    </Routes >
  )


}

export default App;
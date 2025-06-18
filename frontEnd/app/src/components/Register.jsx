import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { userAPI } from './Api.jsx';
import '../styles/Register.css';

function Register() {
  console.log('🔍 Register rendering...');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    console.log('username:', username);
    console.log('password:', password);
    try {
      const data = await userAPI.register(username, password);
      if (data) {
        console.log('Registration', data.userId);
        localStorage.setItem('username', username.trim());
        localStorage.setItem('userId', data.userId);
      }
      setTimeout(() => {
        navigate('/main');
      }, 1000);
    } catch (error) {
      // Error handling is already done in the centralized API
      console.error('Registration failed:', error.message);
    }
  };

  return (
    <>
      <div className="register-container">
        <h1>Balapao Talk</h1>
        <form onSubmit={handleRegister} className="form">
          <div className="form-row">
            <label htmlFor="username">Username: </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>

          <div className="form-row">
            <label htmlFor="password">Password: </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <div className="button-container">
            <button type="submit">Register</button>
          </div>
        </form>

      </div>
    </>
  );
}

export default Register;

import { useState } from 'react';
import { useNavigate } from 'react-router-dom'

import '../styles/Register.css';

function Register() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleRegister = (e) => {
    e.preventDefault();
    console.log('username:', username);
    console.log('password:', password);
    navigate('/main');
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

          <div className="a-container">
            <button type="submit">Register</button>
          </div>
        </form>

      </div>
    </>
  );
}

export default Register;

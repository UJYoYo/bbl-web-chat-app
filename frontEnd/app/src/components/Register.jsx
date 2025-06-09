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
      <h1>Prajogo Talk</h1>
      <div className="register-container">
        <form onSubmit={handleRegister}>
          <div className="form">
            <label htmlFor="username">Username: </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>

          <div className="form">
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

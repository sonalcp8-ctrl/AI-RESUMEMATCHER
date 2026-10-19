import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Briefcase, Mail, Lock, LogIn, AlertCircle } from 'lucide-react';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login, loading } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!email || !password) {
      setError('Please fill in both email and password');
      return;
    }
    const res = await login(email, password);
    if (res.success) {
      navigate('/');
    } else {
      setError(res.message);
    }
  };

  const handleQuickLogin = (demoEmail, demoPassword) => {
    setEmail(demoEmail);
    setPassword(demoPassword);
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-header">
          <div className="auth-logo-icon">
            <Briefcase size={28} color="#fff" />
          </div>
          <h2>Welcome Back</h2>
          <p>Login to match your skills with top job opportunities</p>
        </div>

        {error && (
          <div className="alert-box alert-error">
            <AlertCircle size={18} />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label>Email Address</label>
            <div className="input-with-icon">
              <Mail size={18} className="input-icon" />
              <input
                type="email"
                placeholder="name@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label>Password</label>
            <div className="input-with-icon">
              <Lock size={18} className="input-icon" />
              <input
                type="password"
                placeholder="Enter password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
          </div>

          <button type="submit" disabled={loading} className="btn-primary btn-full">
            <LogIn size={18} />
            <span>{loading ? 'Logging in...' : 'Sign In'}</span>
          </button>
        </form>

        <div className="demo-accounts">
          <p className="demo-title">⚡ Quick Demo Test Accounts:</p>
          <div className="demo-buttons">
            <button
              type="button"
              className="demo-btn"
              onClick={() => handleQuickLogin('sonal@gmail.com', '123456')}
            >
              👤 Sonal (sonal@gmail.com)
            </button>
            <button
              type="button"
              className="demo-btn"
              onClick={() => handleQuickLogin('rahul@gmail.com', 'rahul123')}
            >
              👤 Rahul (rahul@gmail.com)
            </button>
          </div>
        </div>

        <div className="auth-footer">
          Don't have an account? <Link to="/register">Create Account</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;

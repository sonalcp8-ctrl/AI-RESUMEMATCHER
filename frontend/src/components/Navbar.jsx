import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Briefcase, CheckCircle, FileText, User, LogOut, Award } from 'lucide-react';

const Navbar = () => {
  const { user, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  if (!user) return null;

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const navLinks = [
    { name: 'Jobs & Matching', path: '/', icon: Briefcase },
    { name: 'My Skills', path: '/skills', icon: Award },
    { name: 'My Applications', path: '/applications', icon: CheckCircle },
    { name: 'My Profile', path: '/profile', icon: User },
  ];

  return (
    <header className="navbar">
      <div className="nav-container">
        <Link to="/" className="nav-logo">
          <div className="logo-icon">
            <Briefcase size={22} color="#fff" />
          </div>
          <span className="logo-text">Resume<span className="logo-accent">Matcher</span></span>
        </Link>

        <nav className="nav-links">
          {navLinks.map((link) => {
            const Icon = link.icon;
            const active = location.pathname === link.path;
            return (
              <Link
                key={link.path}
                to={link.path}
                className={`nav-link ${active ? 'nav-link-active' : ''}`}
              >
                <Icon size={18} />
                <span>{link.name}</span>
              </Link>
            );
          })}
        </nav>

        <div className="nav-user-section">
          <Link to="/profile" className="user-badge" title={user.fullName}>
            <div className="avatar">
              {user.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
            </div>
            <div className="user-info-text">
              <span className="user-name">{user.fullName || 'User'}</span>
              <span className="user-email">{user.email}</span>
            </div>
          </Link>
          <button onClick={handleLogout} className="btn-logout" title="Logout">
            <LogOut size={18} />
            <span className="logout-text">Logout</span>
          </button>
        </div>
      </div>
    </header>
  );
};

export default Navbar;

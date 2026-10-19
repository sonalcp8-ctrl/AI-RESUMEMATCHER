import React, { createContext, useContext, useState, useEffect } from 'react';
import { authApi } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('rm_user');
    return saved ? JSON.parse(saved) : null;
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (user && user.id) {
      // Sync fresh profile data on startup
      refreshUser(user.id);
    }
  }, []);

  const refreshUser = async (userId) => {
    try {
      const res = await authApi.getProfile(userId || user.id);
      if (res.data?.success && res.data.data) {
        setUser(res.data.data);
        localStorage.setItem('rm_user', JSON.stringify(res.data.data));
      }
    } catch (err) {
      console.error('Failed to sync profile', err);
    }
  };

  const login = async (email, password) => {
    setLoading(true);
    try {
      const res = await authApi.login(email, password);
      if (res.data?.success && res.data.data) {
        setUser(res.data.data);
        localStorage.setItem('rm_user', JSON.stringify(res.data.data));
        return { success: true };
      }
      return { success: false, message: res.data?.message || 'Login failed' };
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Login failed';
      return { success: false, message: msg };
    } finally {
      setLoading(false);
    }
  };

  const register = async (fullName, email, password, phone) => {
    setLoading(true);
    try {
      const res = await authApi.register(fullName, email, password, phone);
      if (res.data?.success && res.data.data) {
        setUser(res.data.data);
        localStorage.setItem('rm_user', JSON.stringify(res.data.data));
        return { success: true };
      }
      return { success: false, message: res.data?.message || 'Registration failed' };
    } catch (err) {
      const msg = err.response?.data?.message || err.message || 'Registration failed';
      return { success: false, message: msg };
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('rm_user');
  };

  return (
    <AuthContext.Provider value={{ user, setUser, loading, login, register, logout, refreshUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

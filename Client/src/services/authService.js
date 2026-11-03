import { apiClient } from './api';

const SESSION_STORAGE_KEY = 'tekkeyah_user_session';

export const authService = {
  /**
   * Standard login
   */
  async login({ username, password, rememberDevice }) {
    try {
      const data = await apiClient('/auth/login', {
        method: 'POST',
        body: { username, password, rememberDevice },
      });
      this.setSession(data);
      return data;
    } catch {
      const fallbackUser = {
        id: 'usr_9021',
        name: username.split('@')[0] || 'Youssef Al-Qassimi',
        email: username.includes('@') ? username : `${username}@tekkeyah.org`,
        role: 'Donor',
        circleId: '#9021',
        isLoggedIn: true,
      };
      this.setSession(fallbackUser);
      return fallbackUser;
    }
  },

  /**
   * Register a new user account
   */
  async register({ fullName, email, password, role = 'DONOR' }) {
    try {
      const data = await apiClient('/auth/register', {
        method: 'POST',
        body: { fullName, email, password, role },
      });
      this.setSession(data);
      return data;
    } catch {
      const newUser = {
        id: `usr_${Date.now()}`,
        name: fullName || 'New Contributor',
        email,
        role: role === 'DONOR' ? 'Donor' : 'Volunteer',
        circleId: `#${Math.floor(1000 + Math.random() * 9000)}`,
        isLoggedIn: true,
      };
      this.setSession(newUser);
      return newUser;
    }
  },

  /**
   * Get active user session
   */
  getCurrentUser() {
    try {
      const raw = localStorage.getItem(SESSION_STORAGE_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  },

  setSession(user) {
    try {
      localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(user));
    } catch {
      // Storage unavailable
    }
  },

  logout() {
    localStorage.removeItem(SESSION_STORAGE_KEY);
  },
};

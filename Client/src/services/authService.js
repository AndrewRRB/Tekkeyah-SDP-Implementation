import { apiClient } from './api';

const SESSION_STORAGE_KEY = 'tekkeyah_user_session';

export const authService = {
  /**
   * Log in with email + password. Throws with the server's message on failure.
   */
  async login({ email, password }) {
    const user = await apiClient('/auth/login', {
      method: 'POST',
      body: { email, password },
    });
    this.setSession(user);
    return user;
  },

  /**
   * Create an account.
   * role: 'DONOR' | 'VOLUNTEER' | 'BENEFICIARY'
   * channel: 'EMAIL' | 'SMS' | 'WHATSAPP' | 'SOCIAL_MEDIA'
   * countryCode: '+20', phone: national number as typed
   */
  async register({ fullName, email, password, countryCode, phone, channel, role = 'DONOR' }) {
    const user = await apiClient('/auth/signup', {
      method: 'POST',
      body: { fullName, email, password, countryCode, phone, channel, role },
    });
    this.setSession(user);
    return user;
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
    try {
      localStorage.removeItem(SESSION_STORAGE_KEY);
    } catch {
      // Storage unavailable
    }
  },
};

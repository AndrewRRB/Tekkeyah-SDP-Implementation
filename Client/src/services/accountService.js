import { apiClient } from './api';
import { authService } from './authService';

/**
 * Account settings (User Control Panel). Every call that returns the user
 * also refreshes the stored session so the navbar / other pages stay in sync.
 */
const withSession = async (request) => {
  const user = await request;
  authService.setSession(user);
  return user;
};

export const accountService = {
  getAccount(userId) {
    return withSession(apiClient(`/account/${userId}`));
  },

  updateProfile(userId, { fullName, email, countryCode, phone }) {
    return withSession(
      apiClient(`/account/${userId}/profile`, {
        method: 'PUT',
        body: { fullName, email, countryCode, phone },
      }),
    );
  },

  changeChannel(userId, channel) {
    return withSession(
      apiClient(`/account/${userId}/channel`, { method: 'PUT', body: { channel } }),
    );
  },

  changePassword(userId, { currentPassword, newPassword }) {
    return apiClient(`/account/${userId}/password`, {
      method: 'PUT',
      body: { currentPassword, newPassword },
    });
  },

  async deleteAccount(userId, password) {
    await apiClient(`/account/${userId}`, { method: 'DELETE', body: { password } });
    authService.logout();
  },
};

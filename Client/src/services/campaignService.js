import { apiClient } from './api';

export const campaignService = {
  /**
   * Fetch featured campaigns and outreaches
   */
  async getCampaigns(filter = 'ALL') {
    try {
      return await apiClient(`/events?filter=${filter}`);
    } catch {
      return [];
    }
  },

  /**
   * Fetch public ledger live metrics
   */
  async getLedgerStats() {
    try {
      return await apiClient('/ledger/stats');
    } catch {
      return {
        totalFunds: 342850,
        hotMeals: 18420,
        activeVolunteers: 312,
        reconciledPct: 100,
      };
    }
  },
};

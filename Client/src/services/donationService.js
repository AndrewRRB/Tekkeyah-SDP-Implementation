import { apiClient } from './api';

/**
 * Every call hits the Spring backend; nothing is faked on failure.
 * Responses are DonationResponse objects:
 * { donationId, type, amount, description, items, mealWeight, transactionRef,
 *   paymentMethod, receiptId, receiptNumber, issuedAt, userId }
 */
export const donationService = {
  /**
   * paymentMethod: 'CARD' | 'CASH' | 'CHECK'
   * card (CARD only): { holderName, number, expiryMonth, expiryYear, cvv }.
   * The server keeps only the brand + last four digits.
   */
  submitMonetaryDonation({ userId, amount, paymentMethod, card }) {
    return apiClient('/donations/monetary', {
      method: 'POST',
      body: { userId, amount, paymentMethod, card: paymentMethod === 'CARD' ? card : null },
    });
  },

  submitMealDonation({ userId, name, mealWeight = 0, mealCost }) {
    return apiClient('/donations/meal', {
      method: 'POST',
      body: { userId, name, mealWeight, mealCost },
    });
  },

  submitInKindDonation({ userId, items, estimatedValue }) {
    return apiClient('/donations/in-kind', {
      method: 'POST',
      body: { userId, items, estimatedValue },
    });
  },

  /**
   * All donations of one user, newest first.
   */
  getDonationHistory(userId) {
    return apiClient(`/donations?userId=${encodeURIComponent(userId)}`);
  },
};

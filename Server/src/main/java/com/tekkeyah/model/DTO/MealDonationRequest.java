package com.tekkeyah.model.DTO;

/**
 * POST /api/donations/meal body. Mirrors MealDonation: name, mealWeight (kg),
 * mealCost (the donation's value).
 */
public record MealDonationRequest(Integer userId, String name, double mealWeight, double mealCost) {
}

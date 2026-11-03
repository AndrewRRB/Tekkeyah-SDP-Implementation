package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Donation;
import com.tekkeyah.model.entity.MealDonation;

public class MealDonationCreator extends DonationCreator {

    private double mealWeight;
    private double mealCost;

    public MealDonationCreator() {
        super();
    }

    public MealDonationCreator(double mealWeight, double mealCost) {
        super();
        this.mealWeight = mealWeight;
        this.mealCost = mealCost;
    }

    @Override
    public Donation createDonation() {
        MealDonation donation = new MealDonation();
        donation.setMealWeight(this.mealWeight);
        donation.setMealCost(this.mealCost);
        donation.setAmount(this.mealCost);
        return donation;
    }

    public double getMealWeight() {
        return mealWeight;
    }

    public void setMealWeight(double mealWeight) {
        this.mealWeight = mealWeight;
    }

    public double getMealCost() {
        return mealCost;
    }

    public void setMealCost(double mealCost) {
        this.mealCost = mealCost;
    }
}

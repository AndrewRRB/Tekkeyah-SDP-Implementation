package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Donation;
import com.tekkeyah.model.entity.MealDonation;

public class MealDonationCreator extends DonationCreator {
    private String name;
    private double mealWeight;
    private double mealCost;

    public MealDonationCreator() {
        super();
    }

    public MealDonationCreator(String name,double mealWeight, double mealCost) {
        super();
        this.name = name;
        this.mealWeight = mealWeight;
        this.mealCost = mealCost;
    }

    @Override
    public Donation createDonation() {
        MealDonation donation = new MealDonation();
        donation.setName(this.name);
        donation.setMealWeight(this.mealWeight);
        donation.setMealCost(this.mealCost);
        donation.setAmount(this.mealCost);
        return donation;
    }
}

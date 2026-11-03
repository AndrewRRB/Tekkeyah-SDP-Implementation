package com.tekkeyah.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "meal_donations")
public class MealDonation extends Donation {

    private double mealWeight;
    private double mealCost;

    public MealDonation() {
        super();
    }

    public MealDonation(double mealWeight, double mealCost) {
        super(mealCost);
        this.mealWeight = mealWeight;
        this.mealCost = mealCost;
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
        this.amount = mealCost;
    }

    @Override
    public void process() {
        if (this.amount == 0.0 && this.mealCost > 0) {
            this.amount = this.mealCost;
        }
        System.out.println("Processing meal donation: Weight = " + mealWeight + " kg, Cost = $" + mealCost);
        // Depends on Subject/Observer from outside the donation classes section:
        // notifyObservers("Meal donation processed: Weight = " + mealWeight + " kg, Cost = $" + mealCost);
    }

    @Override
    public Receipt generateReceipt() {
        Receipt r = new Receipt(this);
        this.receipt = r;
        return r;
    }
}

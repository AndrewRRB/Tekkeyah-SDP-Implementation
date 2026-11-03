package com.tekkeyah.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "meal_donations")
public class MealDonation extends Donation {
    private String name;
    private double mealWeight;
    private double mealCost;

    public MealDonation() {
        super();
    }

    public MealDonation(String name, double mealWeight, double mealCost) {
        super(mealCost);
        this.name = name;
        this.mealWeight = mealWeight;
        this.mealCost = mealCost;
    }
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

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
        System.out.println("Processing meal donation: Weight = " + mealWeight + " kg, Cost = $" + mealCost);
        notifyObservers("Meal donation processed: Weight = " + mealWeight + " kg, Cost = $" + mealCost);
    }
}

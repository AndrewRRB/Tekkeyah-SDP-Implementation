package com.tekkeyah.model.entity;
import jakarta.persistence.Entity;

@Entity
public class Fundraiser extends Event{
    private double amountRaised;
    private double fundTarget;

    public Fundraiser(){}


    //methods
    @Override
    public String getEventType(){
        return "Fundraiser";
    }

    @Override
    public String getSummary(){
        return super.getSummary() + " - raised " + amountRaised + " of " + fundTarget;
    }

    @Override
    public String getReminderMessage() {
        return "Fundraiser reminder: " + title + " starts at " + startDateTime;
    }

    public void recordDonation(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        amountRaised += amount;
        if (amountRaised >= fundTarget) {
            notifyObservers("Fundraiser " + title + " reached its target!");
        }
    }

    public double getProgressPercent() {
        return fundTarget == 0 ? 0 : (amountRaised / fundTarget) * 100;
    }

    //setters and getters
    public double getAmountRaised() {
        return amountRaised;
    }

    public void setAmountRaised(double amountRaised){
        this.amountRaised = amountRaised;
    }

    public double getFundTarget(){
        return fundTarget;
    }

    public void setFundTarget(double fundTarget){
        this.fundTarget = fundTarget;
    }
}

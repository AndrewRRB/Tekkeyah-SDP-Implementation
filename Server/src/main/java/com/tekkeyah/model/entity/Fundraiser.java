package com.tekkeyah.model.entity;
import jakarta.persistence.Entity;

@Entity
public class Fundraiser extends Event{
    private double amountRaised;
    private double fundTarget;

    public Fundraiser(){}

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

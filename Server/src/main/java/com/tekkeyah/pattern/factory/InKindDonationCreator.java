package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Donation;
import com.tekkeyah.model.entity.InKindDonation;

public class InKindDonationCreator extends DonationCreator {

    private String[] itemsDonated;
    private double estimatedValue;

    public InKindDonationCreator() {
        super();
    }

    public InKindDonationCreator(String[] itemsDonated) {
        super();
        this.itemsDonated = itemsDonated;
    }

    public InKindDonationCreator(String[] itemsDonated, double estimatedValue) {
        super();
        this.itemsDonated = itemsDonated;
        this.estimatedValue = estimatedValue;
    }

    @Override
    public Donation createDonation() {
        InKindDonation donation = new InKindDonation();
        donation.setItemsDonated(this.itemsDonated);
        donation.setAmount(this.estimatedValue);
        return donation;
    }

    public String[] getItemsDonated() {
        return itemsDonated;
    }

    public void setItemsDonated(String[] itemsDonated) {
        this.itemsDonated = itemsDonated;
    }

    public double getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }
}

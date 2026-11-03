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
        return new InKindDonation(this.itemsDonated, this.estimatedValue);
    }
}

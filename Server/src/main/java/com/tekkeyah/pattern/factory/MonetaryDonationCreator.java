package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Donation;
import com.tekkeyah.model.entity.MonetaryDonation;

public class MonetaryDonationCreator extends DonationCreator {

    private double amount;

    public MonetaryDonationCreator() {
        super();
    }

    public MonetaryDonationCreator(double amount) {
        super();
        this.amount = amount;
    }

    @Override
    public Donation createDonation() {
        MonetaryDonation donation = new MonetaryDonation();
        donation.setAmount(this.amount);
        return donation;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

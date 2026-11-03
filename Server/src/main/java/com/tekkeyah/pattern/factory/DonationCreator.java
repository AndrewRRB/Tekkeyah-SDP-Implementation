package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Donation;
import java.util.Observer;

public abstract class DonationCreator {

    private Observer observer;

    public DonationCreator() {}

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public abstract Donation createDonation();

    public Donation makeDonation() {
        Donation d = createDonation();
        if (observer != null) {
            d.addObserver(observer);
        }
        d.process();
        d.generateReceipt();
        return d;
    }
}

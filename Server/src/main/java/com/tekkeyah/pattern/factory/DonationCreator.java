package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Donation;

// Depends on Observer from outside the donation classes section (Observer pattern)
// import com.tekkeyah.pattern.observer.Observer;

public abstract class DonationCreator {

    // Depends on Observer from outside the donation classes section
    // private Observer observer;

    public DonationCreator() {}

    public abstract Donation createDonation();

    public Donation makeDonation() {
        Donation d = createDonation();
        // Depends on Observer from outside the donation classes section:
        // d.attach(observer);
        d.process();
        d.generateReceipt();
        return d;
    }

    // Depends on Observer from outside the donation classes section
    // public Observer getObserver() {
    //     return observer;
    // }
    // public void setObserver(Observer observer) {
    //     this.observer = observer;
    // }
}

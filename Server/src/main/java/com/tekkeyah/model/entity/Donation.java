package com.tekkeyah.model.entity;

import jakarta.persistence.*;

// Depends on Subject and Observer from outside the donation classes section (Observer pattern)
// import com.tekkeyah.pattern.observer.Subject;
// import com.tekkeyah.pattern.observer.Observer;

// Depends on Donor and Beneficiary from outside the donation classes section (User hierarchy)
// import com.tekkeyah.model.entity.Donor;
// import com.tekkeyah.model.entity.Beneficiary;

@Entity
@Table(name = "donations")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Donation /* implements Subject */ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int donationID;

    protected double amount;

    // Depends on Donor from outside the donation classes section (User hierarchy)
    // protected Donor donor;

    // Depends on Beneficiary from outside the donation classes section (User hierarchy)
    // protected Beneficiary beneficiary;

    // Depends on Observer from outside the donation classes section (Observer pattern)
    // private List<Observer> observers;

    @OneToOne(mappedBy = "donation", cascade = CascadeType.ALL)
    protected Receipt receipt;

    public Donation() {}

    public Donation(double amount) {
        this.amount = amount;
    }

    public abstract void process();

    public abstract Receipt generateReceipt();

    public int getDonationID() {
        return donationID;
    }

    public void setDonationID(int donationID) {
        this.donationID = donationID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    // Depends on Donor from outside the donation classes section (User hierarchy)
    // public Donor getDonor() {
    //     return donor;
    // }
    // public void setDonor(Donor donor) {
    //     this.donor = donor;
    // }

    // Depends on Beneficiary from outside the donation classes section (User hierarchy)
    // public Beneficiary getBeneficiary() {
    //     return beneficiary;
    // }
    // public void setBeneficiary(Beneficiary beneficiary) {
    //     this.beneficiary = beneficiary;
    // }

    // Depends on Observer / Subject from outside the donation classes section (Observer pattern)
    // public void attach(Observer o) {
    //     if (o != null && !observers.contains(o)) {
    //         observers.add(o);
    //     }
    // }
    // public void detach(Observer o) {
    //     observers.remove(o);
    // }
    // public void notifyObservers(String msg) {
    //     for (Observer o : observers) {
    //         o.update(this, msg);
    //     }
    // }
}

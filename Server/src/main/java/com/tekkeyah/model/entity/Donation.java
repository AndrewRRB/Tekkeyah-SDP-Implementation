package com.tekkeyah.model.entity;

import jakarta.persistence.*;

import java.util.Observable;
import java.util.Observer;

// Depends on Donor and Beneficiary from outside the donation classes section (User hierarchy)
// import com.tekkeyah.model.entity.Donor;
// import com.tekkeyah.model.entity.Beneficiary;

@Entity
@Table(name = "donations")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Donation extends Observable/* implements Subject */ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int donationID;

    protected double amount;

    // Depends on Donor from outside the donation classes section (User hierarchy)
    // protected Donor donor;

    // Depends on Beneficiary from outside the donation classes section (User hierarchy)
    // protected Beneficiary beneficiary;

    @OneToOne(mappedBy = "donation", cascade = CascadeType.ALL)
    protected Receipt receipt;

    public Donation() {}

    public Donation(double amount) {
        this.amount = amount;
    }

    public abstract void process();

    public int getDonationID() {
        return donationID;
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

     public void attach(Observer o) {
         this.addObserver(o);
     }
     public void detach(Observer o) {
        this.deleteObserver(o);
     }
     public void notifyObservers(String msg) {
         setChanged();
         super.notifyObservers(msg);
     }

    public Receipt generateReceipt() {
        Receipt r = new Receipt(this);
        this.receipt = r;
        return r;
    }
}

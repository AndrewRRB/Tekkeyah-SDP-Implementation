package com.tekkeyah.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;

// Depends on DonationPayment from outside the donation classes section (Payment hierarchy)
// import com.tekkeyah.model.entity.DonationPayment;

@Entity
@Table(name = "monetary_donations")
public class MonetaryDonation extends Donation {

    // Depends on DonationPayment from outside the donation classes section (Payment hierarchy)
    // private DonationPayment payment;

    private String transactionRefID;

    public MonetaryDonation() {
        super();
    }

    public MonetaryDonation(double amount) {
        super(amount);
    }

    // Depends on DonationPayment from outside the donation classes section (Payment hierarchy)
    // public DonationPayment getPayment() {
    //     return payment;
    // }
    // public void setPayment(DonationPayment payment) {
    //     this.payment = payment;
    // }

    public String getTransactionRefID() {
        return transactionRefID;
    }

    public void setTransactionRefID(String transactionRefID) {
        this.transactionRefID = transactionRefID;
    }

    @Override
    public void process() {
        // Depends on payment from outside the donation classes section:
        // if (payment != null) {
        //     payment.process();
        // }
        if (this.transactionRefID == null || this.transactionRefID.isEmpty()) {
            this.transactionRefID = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        System.out.println("Processing monetary donation of amount: $" + amount + " with Ref ID: " + transactionRefID);
        // Depends on Subject/Observer from outside the donation classes section:
        // notifyObservers("Monetary donation of $" + amount + " processed. Ref: " + transactionRefID);
    }

    @Override
    public Receipt generateReceipt() {
        Receipt r = new Receipt(this);
        this.receipt = r;
        return r;
    }
}

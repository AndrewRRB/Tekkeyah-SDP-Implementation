package com.tekkeyah.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int receiptID;

    @OneToOne
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation;

    private LocalDateTime issuedAt;

    public Receipt() {
        this.issuedAt = LocalDateTime.now();
    }

    public Receipt(Donation donation) {
        this.donation = donation;
        this.issuedAt = LocalDateTime.now();
    }

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String toText() {
        int dId = (donation != null) ? donation.getDonationID() : 0;
        double amt = (donation != null) ? donation.getAmount() : 0.0;
        String type = (donation != null) ? donation.getClass().getSimpleName() : "Donation";

        return "Receipt ID: " + receiptID + "\n" +
               "Donation ID: " + dId + "\n" +
               "Donation Type: " + type + "\n" +
               "Amount: $" + amt + "\n" +
               "Issued At: " + issuedAt;
    }

    public void download() {
        System.out.println("Downloading receipt #" + receiptID + ":\n" + toText());
    }
}

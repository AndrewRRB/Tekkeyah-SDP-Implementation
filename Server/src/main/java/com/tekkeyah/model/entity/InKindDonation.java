package com.tekkeyah.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "inkind_donations")
public class InKindDonation extends Donation {

    @Column(name = "items_donated")
    private String[] itemsDonated;

    public InKindDonation() {
        super();
    }

    public InKindDonation(String[] itemsDonated) {
        super();
        this.itemsDonated = itemsDonated;
    }

    public InKindDonation(String[] itemsDonated, double estimatedValue) {
        super(estimatedValue);
        this.itemsDonated = itemsDonated;
    }

    public String[] getItemsDonated() {
        return itemsDonated;
    }

    public void setItemsDonated(String[] itemsDonated) {
        this.itemsDonated = itemsDonated;
    }

    @Override
    public void process() {
        String items = (itemsDonated != null && itemsDonated.length > 0)
                ? String.join(", ", itemsDonated)
                : "None";
        System.out.println("Processing in-kind donation with items: " + items);
        // Depends on Subject/Observer from outside the donation classes section:
        // notifyObservers("In-kind donation processed with items: " + items);
    }

    @Override
    public Receipt generateReceipt() {
        Receipt r = new Receipt(this);
        this.receipt = r;
        return r;
    }
}

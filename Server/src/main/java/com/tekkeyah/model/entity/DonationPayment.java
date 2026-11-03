package com.tekkeyah.model.entity;

import com.tekkeyah.pattern.strategy.PaymentStrategy;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "donation_payments")
public class DonationPayment extends Payment {

    public DonationPayment() {
        super();
    }

    public DonationPayment(double amount, PaymentStrategy method) {
        super(amount, method);
    }
}

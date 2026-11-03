package com.tekkeyah.pattern.strategy;

public class CashPayment implements PaymentStrategy {
    private String receivedBy;

    public CashPayment() {}

    public CashPayment(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    @Override
    public boolean pay(double amount) {
        return amount > 0;
    }

    @Override
    public boolean refund(double amount) {
        return amount > 0;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }
}

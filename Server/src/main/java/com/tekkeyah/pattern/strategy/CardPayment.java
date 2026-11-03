package com.tekkeyah.pattern.strategy;

public class CardPayment implements PaymentStrategy {
    private String cardLastFour;

    public CardPayment() {}

    public CardPayment(String cardLastFour) {
        this.cardLastFour = cardLastFour;
    }

    @Override
    public boolean pay(double amount) {
        return amount > 0;
    }

    @Override
    public boolean refund(double amount) {
        return amount > 0;
    }

    public String getCardLastFour() {
        return cardLastFour;
    }

    public void setCardLastFour(String cardLastFour) {
        this.cardLastFour = cardLastFour;
    }
}

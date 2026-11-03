package com.tekkeyah.pattern.strategy;

public class CheckPayment implements PaymentStrategy {
    private String checkNumber;
    private String bankName;

    public CheckPayment() {}

    public CheckPayment(String checkNumber, String bankName) {
        this.checkNumber = checkNumber;
        this.bankName = bankName;
    }

    @Override
    public boolean pay(double amount) {
        return amount > 0;
    }

    @Override
    public boolean refund(double amount) {
        return amount > 0;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}

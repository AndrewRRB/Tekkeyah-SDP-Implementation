package com.tekkeyah.pattern.strategy;

public interface PaymentStrategy {
    boolean pay(double amount);
    boolean refund(double amount);
}

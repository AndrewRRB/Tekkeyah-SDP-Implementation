package com.tekkeyah.model.enums;

/**
 * Payment method a donor chooses for a monetary donation. DonationService maps
 * each value to its PaymentStrategy (CardPayment / CashPayment / CheckPayment).
 */
public enum PaymentMethod {
    CARD,
    CASH,
    CHECK
}

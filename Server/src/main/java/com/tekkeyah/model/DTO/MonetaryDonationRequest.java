package com.tekkeyah.model.DTO;

import com.tekkeyah.model.enums.PaymentMethod;

/** POST /api/donations/monetary body. paymentMethod defaults to CARD. */
public record MonetaryDonationRequest(Integer userId, double amount, PaymentMethod paymentMethod) {
}

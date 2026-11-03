package com.tekkeyah.model.DTO;

import java.util.List;

/** POST /api/donations/in-kind body. Mirrors InKindDonation: items + estimated value. */
public record InKindDonationRequest(Integer userId, List<String> items, double estimatedValue) {
}

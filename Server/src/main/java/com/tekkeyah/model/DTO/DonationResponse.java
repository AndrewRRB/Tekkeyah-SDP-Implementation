package com.tekkeyah.model.DTO;

import com.tekkeyah.model.entity.Donation;
import com.tekkeyah.model.entity.InKindDonation;
import com.tekkeyah.model.entity.MealDonation;
import com.tekkeyah.model.entity.MonetaryDonation;
import com.tekkeyah.model.entity.Receipt;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * PATTERN : DTO (Data Transfer Object)
 * PART    : What the server sends back about one donation and its receipt
 * ----------------------------------------------------------------------------
 * Flat snapshot so the Donation <-> Receipt <-> DonorProfile <-> User graph
 * never reaches Jackson. Every value comes from the saved rows.
 * "paymentMethod" is only known right after a monetary donation is made
 * (the strategy object is not stored), so it is null in history results.
 * ============================================================================
 */
public record DonationResponse(int donationId,
                               String type,
                               double amount,
                               String description,
                               List<String> items,
                               Double mealWeight,
                               String transactionRef,
                               String paymentMethod,
                               int receiptId,
                               String receiptNumber,
                               LocalDateTime issuedAt,
                               int userId) {

    public static DonationResponse from(Donation d, int userId, String paymentMethod) {
        Receipt r = d.getReceipt();
        int receiptId = (r != null) ? r.getReceiptID() : 0;
        String receiptNumber = (r != null) ? String.format("TK-%06d", receiptId) : null;
        LocalDateTime issuedAt = (r != null) ? r.getIssuedAt() : null;

        if (d instanceof MonetaryDonation m) {
            return new DonationResponse(d.getDonationID(), "Monetary", d.getAmount(),
                    "Monetary gift", List.of(), null, m.getTransactionRefID(), paymentMethod,
                    receiptId, receiptNumber, issuedAt, userId);
        }
        if (d instanceof MealDonation meal) {
            return new DonationResponse(d.getDonationID(), "Meal", d.getAmount(),
                    meal.getName(), List.of(), meal.getMealWeight(), null, null,
                    receiptId, receiptNumber, issuedAt, userId);
        }
        if (d instanceof InKindDonation inKind) {
            List<String> items = (inKind.getItemsDonated() != null)
                    ? Arrays.asList(inKind.getItemsDonated()) : List.of();
            return new DonationResponse(d.getDonationID(), "In-Kind", d.getAmount(),
                    String.join(", ", items), items, null, null, null,
                    receiptId, receiptNumber, issuedAt, userId);
        }
        return new DonationResponse(d.getDonationID(), d.getClass().getSimpleName(), d.getAmount(),
                null, List.of(), null, null, null, receiptId, receiptNumber, issuedAt, userId);
    }
}

package com.tekkeyah.controller;

import com.tekkeyah.model.DTO.DonationResponse;
import com.tekkeyah.model.DTO.InKindDonationRequest;
import com.tekkeyah.model.DTO.MealDonationRequest;
import com.tekkeyah.model.DTO.MonetaryDonationRequest;
import com.tekkeyah.service.DonationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ENDPOINTS (each POST goes through the FACTORY METHOD creator for its type)
 *   POST /api/donations/monetary       money via card / cash / check (STRATEGY)
 *   POST /api/donations/meal           meal donation
 *   POST /api/donations/in-kind        goods / supplies
 *   GET  /api/donations?userId={id}    one user's donations + receipts, newest first
 *   GET  /api/donations/{id}           one donation + its receipt
 */
@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping("/monetary")
    public ResponseEntity<DonationResponse> monetary(@RequestBody MonetaryDonationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(donationService.donateMoney(req));
    }

    @PostMapping("/meal")
    public ResponseEntity<DonationResponse> meal(@RequestBody MealDonationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(donationService.donateMeal(req));
    }

    @PostMapping("/in-kind")
    public ResponseEntity<DonationResponse> inKind(@RequestBody InKindDonationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(donationService.donateInKind(req));
    }

    @GetMapping
    public List<DonationResponse> history(@RequestParam int userId) {
        return donationService.historyFor(userId);
    }

    @GetMapping("/{id}")
    public DonationResponse one(@PathVariable int id) {
        return donationService.getDonation(id);
    }
}

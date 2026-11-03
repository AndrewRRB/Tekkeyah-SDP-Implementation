package com.tekkeyah.service;

import com.tekkeyah.exception.ResourceNotFoundException;
import com.tekkeyah.model.DTO.DonationResponse;
import com.tekkeyah.model.DTO.InKindDonationRequest;
import com.tekkeyah.model.DTO.MealDonationRequest;
import com.tekkeyah.model.DTO.MonetaryDonationRequest;
import com.tekkeyah.model.entity.Donation;
import com.tekkeyah.model.entity.DonorProfile;
import com.tekkeyah.model.entity.MonetaryDonation;
import com.tekkeyah.model.entity.Payment;
import com.tekkeyah.model.entity.User;
import com.tekkeyah.model.enums.PaymentMethod;
import com.tekkeyah.pattern.factory.DonationCreator;
import com.tekkeyah.pattern.factory.DonationPaymentCreator;
import com.tekkeyah.pattern.factory.InKindDonationCreator;
import com.tekkeyah.pattern.factory.MealDonationCreator;
import com.tekkeyah.pattern.factory.MonetaryDonationCreator;
import com.tekkeyah.pattern.strategy.CardPayment;
import com.tekkeyah.pattern.strategy.CashPayment;
import com.tekkeyah.pattern.strategy.CheckPayment;
import com.tekkeyah.pattern.strategy.PaymentStrategy;
import com.tekkeyah.repository.DonationRepository;
import com.tekkeyah.repository.DonorProfileRepository;
import com.tekkeyah.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ============================================================================
 * PATTERNS : FACTORY METHOD (DonationCreator / DonationPaymentCreator pick the
 *            concrete type), STRATEGY (PaymentStrategy per payment method),
 *            OBSERVER (the donor's User is attached to the Payment and the
 *            Donation, so it is told through its NotificationStrategy)
 * PART     : Making donations and reading a donor's history
 * ----------------------------------------------------------------------------
 * FLOW (one transaction, so either everything is saved or nothing is)
 *   1. load the donor's User (strategy restored) and its DonorProfile
 *      (created on the fly if this user never donated: composition)
 *   2. monetary only: DonationPaymentCreator.createPayment() + strategy,
 *      process(), save the payment row; its id becomes the transaction ref
 *   3. creator.makeDonation(): create -> attach observer -> process -> receipt
 *   4. link donation -> donor (UML: Donation.donor), add to totalDonated,
 *      save the donation (the Receipt is saved with it by cascade)
 * ============================================================================
 */
@Service
@Transactional
public class DonationService {

    private final DonationRepository donationRepository;
    private final DonorProfileRepository donorProfileRepository;
    private final PaymentRepository paymentRepository;
    private final DonationPaymentCreator paymentCreator;
    private final UserService userService;

    public DonationService(DonationRepository donationRepository,
                           DonorProfileRepository donorProfileRepository,
                           PaymentRepository paymentRepository,
                           DonationPaymentCreator paymentCreator,
                           UserService userService) {
        this.donationRepository = donationRepository;
        this.donorProfileRepository = donorProfileRepository;
        this.paymentRepository = paymentRepository;
        this.paymentCreator = paymentCreator;
        this.userService = userService;
    }

    // ------------------------------ CREATE ----------------------------------

    public DonationResponse donateMoney(MonetaryDonationRequest req) {
        requireBody(req);
        requirePositive(req.amount(), "amount");
        PaymentMethod method = (req.paymentMethod() != null) ? req.paymentMethod() : PaymentMethod.CARD;
        User donorUser = userService.loadUser(requireUserId(req.userId()));

        Payment payment = paymentCreator.createPayment();   // factory method -> DonationPayment
        payment.setAmount(req.amount());
        payment.setMethod(strategyFor(method));             // strategy
        payment.attach(donorUser);                          // observer
        if (!payment.process()) {
            throw new IllegalArgumentException("payment was not completed");
        }
        String transactionRef = "PAY-" + paymentRepository.save(payment).getPaymentID();

        DonationCreator creator = new MonetaryDonationCreator(req.amount()) {
            @Override
            public Donation createDonation() {
                MonetaryDonation donation = (MonetaryDonation) super.createDonation();
                donation.setTransactionRefID(transactionRef); // ties donation row to payment row
                return donation;
            }
        };
        return record(creator, donorUser, method.name());
    }

    public DonationResponse donateMeal(MealDonationRequest req) {
        requireBody(req);
        if (req.name() == null || req.name().isBlank()) {
            throw new IllegalArgumentException("meal name must not be blank");
        }
        requirePositive(req.mealCost(), "mealCost");
        if (req.mealWeight() < 0) {
            throw new IllegalArgumentException("mealWeight must be >= 0");
        }
        User donorUser = userService.loadUser(requireUserId(req.userId()));
        return record(new MealDonationCreator(req.name().trim(), req.mealWeight(), req.mealCost()),
                donorUser, null);
    }

    public DonationResponse donateInKind(InKindDonationRequest req) {
        requireBody(req);
        String[] items = (req.items() == null) ? new String[0] : req.items().stream()
                .filter(i -> i != null && !i.isBlank())
                .map(String::trim)
                .toArray(String[]::new);
        if (items.length == 0) {
            throw new IllegalArgumentException("at least one item is required");
        }
        if (req.estimatedValue() < 0) {
            throw new IllegalArgumentException("estimatedValue must be >= 0");
        }
        User donorUser = userService.loadUser(requireUserId(req.userId()));
        return record(new InKindDonationCreator(items, req.estimatedValue()), donorUser, null);
    }

    // ------------------------------- READ -----------------------------------

    @Transactional(readOnly = true)
    public List<DonationResponse> historyFor(int userId) {
        userService.loadUser(userId); // 404 for an unknown user instead of an empty list
        return donationRepository.findAllByDonorUserId(userId).stream()
                .map(d -> DonationResponse.from(d, userId, null))
                .toList();
    }

    @Transactional(readOnly = true)
    public DonationResponse getDonation(int donationId) {
        Donation d = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("donation " + donationId + " not found"));
        int userId = (d.getDonor() != null) ? d.getDonor().getUser().getUserID() : 0;
        return DonationResponse.from(d, userId, null);
    }

    // ------------------------------ helpers ---------------------------------

    /** Factory makes + processes the donation (observer notified, receipt built); then persist. */
    private DonationResponse record(DonationCreator creator, User donorUser, String paymentMethod) {
        DonorProfile donor = donorProfileOf(donorUser);
        creator.setObserver(donorUser);
        Donation donation = creator.makeDonation();
        donation.setDonor(donor);
        donor.addToTotal(donation.getAmount());
        Donation saved = donationRepository.saveAndFlush(donation); // receipt saved by cascade
        return DonationResponse.from(saved, donorUser.getUserID(), paymentMethod);
    }

    /** Any user can donate; the donor role profile is created on their first donation. */
    private DonorProfile donorProfileOf(User user) {
        DonorProfile existing = user.getDonorProfile();
        return (existing != null) ? existing : donorProfileRepository.save(user.becomeDonor());
    }

    private PaymentStrategy strategyFor(PaymentMethod method) {
        return switch (method) {
            case CARD -> new CardPayment();
            case CASH -> new CashPayment();
            case CHECK -> new CheckPayment();
        };
    }

    private static void requireBody(Object req) {
        if (req == null) {
            throw new IllegalArgumentException("request body is required");
        }
    }

    private static int requireUserId(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        return userId;
    }

    private static void requirePositive(double value, String field) {
        if (!(value > 0) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(field + " must be greater than 0");
        }
    }
}

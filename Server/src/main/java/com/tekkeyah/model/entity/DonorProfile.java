package com.tekkeyah.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * ============================================================================
 * PATTERN : COMPOSITION ("role profile") - the User-side half of the design
 *           (also the entry point into the FACTORY METHOD pattern for donations)
 * PART    : DonorProfile, in the USERS box of the diagram
 * ----------------------------------------------------------------------------
 * WHAT IT IS
 *   The "donor" side of a person. Optional (0..1) part of a User, never a
 *   subclass of it.
 *
 * HOW IT CONNECTS
 *   - User.becomeDonor() creates it (filled diamond: it lives and dies with
 *     its User).
 *   - donate(DonationCreator c) is where FACTORY METHOD is used: the donor does
 *     not write "new MonetaryDonation()" - it hands in a creator
 *     (MonetaryDonationCreator / MealDonationCreator / InKindDonationCreator)
 *     and gets back a ready Donation. That keeps this class unaware of the
 *     concrete donation types.
 *   - The Donation it gets back is an OBSERVER SUBJECT; the factory attaches
 *     observers (e.g. this donor's User) before returning it, so the donor is
 *     notified through their chosen NotificationStrategy.
 *   - Receipt and TaxDocument (Donation branch) are read through here:
 *     getReceipts() / the tax document is built from this donor's receipts.
 *
 * WIRED LATER (types live on the Donation branch). Uncomment when merging it:
 *   @OneToMany(mappedBy = "donor") private List<Donation> donations; // 0..*
 *   public Donation donate(DonationCreator c)   // factory call, then
 *                                               // donations.add(d); totalDonated += d.getAmount()
 *   public List<Donation> getDonations()
 *   public List<Receipt> getReceipts()          // map each donation to its receipt
 * ============================================================================
 */
@Entity
@Table(name = "donor_profiles")
public class DonorProfile {

    // Surrogate key (JPA needs an id; the diagram does not show one).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donor_id")
    private int donorID;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "total_donated")
    private double totalDonated;

    public DonorProfile() {}

    public DonorProfile(User user) {
        this.user = user;
    }

    /** Called by donate() after a donation succeeds; keeps the running total. */
    public void addToTotal(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be >= 0");
        }
        this.totalDonated += amount;
    }

    public int getDonorID() { return donorID; }

    public User getUser() { return user; }

    public double getTotalDonated() { return totalDonated; }
}

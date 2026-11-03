package com.tekkeyah.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * ============================================================================
 * PATTERN : COMPOSITION ("role profile") - the User-side half of the design
 * PART    : BeneficiaryProfile, in the USERS box of the diagram
 * ----------------------------------------------------------------------------
 * WHAT IT IS
 *   The "person who receives help" side of a User. Optional (0..1), never a
 *   subclass of User.
 *
 * HOW IT CONNECTS
 *   - User.becomeBeneficiary() creates it (filled diamond: it lives and dies
 *     with its User).
 *   - needs: what the beneficiary has asked for ("winter clothes", "meals").
 *   - ResourceDistribution and Program (BENEFICIARY SERVICES box) point at this
 *     class: a beneficiary "receives" 0..* distributions and is enrolled in
 *     Programs.
 *   - When a Donation is delivered to them, they are told via the User's
 *     NotificationStrategy (the User is the Observer; nothing is duplicated
 *     in this profile).
 *
 * WIRED LATER (types live on the Donation branch). Uncomment when merging it:
 *   @OneToMany private List<Donation> received;     // 0..*
 *   public void receive(Donation d)                 // received.add(d)
 * ============================================================================
 */
@Entity
@Table(name = "beneficiary_profiles")
public class BeneficiaryProfile {

    // Surrogate key (JPA needs an id; the diagram does not show one).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beneficiary_id")
    private int beneficiaryID;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "beneficiary_needs", joinColumns = @JoinColumn(name = "beneficiary_id"))
    @Column(name = "need")
    private List<String> needs = new ArrayList<>();

    public BeneficiaryProfile() {}

    public BeneficiaryProfile(User user) {
        this.user = user;
    }

    public void addNeed(String need) {
        if (need == null || need.isBlank()) {
            throw new IllegalArgumentException("need must not be blank");
        }
        needs.add(need.trim());
    }

    public List<String> getNeeds() {
        return needs;
    }

    public int getBeneficiaryID() { return beneficiaryID; }

    public User getUser() { return user; }
}

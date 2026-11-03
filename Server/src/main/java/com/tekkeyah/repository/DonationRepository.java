package com.tekkeyah.repository;

import com.tekkeyah.model.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Integer> {

    /** All donations made by one user, newest first, with their receipts in one query. */
    @Query("""
            select d from Donation d
            join fetch d.donor dp
            left join fetch d.receipt
            where dp.user.userID = :userId
            order by d.donationID desc
            """)
    List<Donation> findAllByDonorUserId(@Param("userId") int userId);
}

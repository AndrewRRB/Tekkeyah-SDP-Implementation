package com.tekkeyah.repository;

import com.tekkeyah.model.entity.DonorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorProfileRepository extends JpaRepository<DonorProfile, Integer> {
}

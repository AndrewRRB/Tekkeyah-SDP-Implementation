package com.tekkeyah.repository;

import com.tekkeyah.model.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {
}

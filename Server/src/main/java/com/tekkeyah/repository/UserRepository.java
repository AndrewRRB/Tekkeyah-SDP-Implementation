package com.tekkeyah.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tekkeyah.model.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);
}

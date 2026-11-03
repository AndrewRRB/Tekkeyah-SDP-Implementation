package com.tekkeyah.service;

import com.tekkeyah.model.entity.User;
import com.tekkeyah.model.enums.NotificationChannel;
import com.tekkeyah.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UserPersistenceVisibleTest {

    @Autowired private UserRepository userRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @Commit      // keeps the row in the database so you can see it in SSMS
    void insertsOneUserThatStaysInTheDatabase() {
        String email = "db-check-" + System.currentTimeMillis() + "@example.com";
        long before = userRepository.count();

        User user = new User();
        user.setName("DB Check User");
        user.setEmail(email);
        user.setPhone("01000000000");
        user.setNotificationChannel(NotificationChannel.EMAIL);

        User saved = userRepository.save(user);

        entityManager.flush();
        entityManager.clear();      // forces the next read to hit SQL Server, not memory

        assertTrue(saved.getUserID() > 0);
        assertEquals(before + 1, userRepository.count());

        User loaded = userRepository.findById(saved.getUserID()).orElseThrow();
        assertEquals(email, loaded.getEmail());
    }
}
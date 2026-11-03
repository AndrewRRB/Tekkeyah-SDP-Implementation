package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Event;
import com.tekkeyah.model.entity.EventStatus;
import com.tekkeyah.model.entity.Fundraiser;
import com.tekkeyah.repository.EventRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EventPersistenceTest {

    @Autowired private FundraiserCreator fundraiserCreator;
    @Autowired private EventRepository eventRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @Commit      // keeps the row in the database so you can see it in SSMS
    void savesOneFundraiserToTheDatabase() {
        long before = eventRepository.count();

        Fundraiser f = (Fundraiser) fundraiserCreator.createEvent();   // the factory builds it
        f.setTitle("DB connection test");
        f.setDescription("Inserted by EventPersistenceTest");
        f.setCapacity(10);
        f.setStartDateTime(LocalDateTime.now().plusDays(1));
        f.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(2));
        f.setFundTarget(1000);

        Event saved = eventRepository.save(f);                          // INSERT happens here

        entityManager.flush();
        entityManager.clear();                                          // forces the next read to hit SQL Server

        assertTrue(saved.getEventID() > 0);
        assertEquals(before + 1, eventRepository.count());

        Event loaded = eventRepository.findById(saved.getEventID()).orElseThrow();
        assertInstanceOf(Fundraiser.class, loaded);
        assertEquals("DB connection test", loaded.getTitle());
        assertEquals(EventStatus.SCHEDULED, loaded.getStatus());
        assertEquals(1000.0, ((Fundraiser) loaded).getFundTarget(), 0.001);
    }
}
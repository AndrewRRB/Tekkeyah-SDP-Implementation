package com.tekkeyah.model.entity;

import com.tekkeyah.pattern.observer.Observer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EventTest {

    @Test
    void newEvent_defaultsToScheduled() {
        Event e = new Fundraiser();
        assertEquals(EventStatus.SCHEDULED, e.getStatus());
    }

    @Test
    void changeStatus_updatesStatusAndNotifiesObserver() {
        Event e = new Fundraiser();
        e.setTitle("Drive");
        Observer o = mock(Observer.class);
        e.attach(o);

        e.changeStatus(EventStatus.ONGOING);

        assertEquals(EventStatus.ONGOING, e.getStatus());
        verify(o).update(e, "Event Drive is now ONGOING");
    }

    @Test
    void detach_stopsNotifications() {
        Event e = new Workshop();
        Observer o = mock(Observer.class);
        e.attach(o);
        e.detach(o);

        e.changeStatus(EventStatus.CANCELLED);

        verify(o, never()).update(any(), anyString());
    }

    @Test
    void attachSameObserverTwice_notifiesOnce() {
        Event e = new Workshop();
        e.setTitle("W");
        Observer o = mock(Observer.class);
        e.attach(o);
        e.attach(o);

        e.changeStatus(EventStatus.COMPLETED);

        verify(o, times(1)).update(any(), anyString());
    }

    @Test
    void recordAttendance_reducesAvailableSeats() {
        Event e = new Workshop();
        e.setCapacity(2);

        e.recordAttendance(mock(User.class));

        assertEquals(1, e.getAvailableSeats());
    }

    @Test
    void recordAttendance_whenFull_throws() {
        Event e = new Workshop();
        e.setCapacity(1);
        e.recordAttendance(mock(User.class));

        assertThrows(IllegalStateException.class,
                () -> e.recordAttendance(mock(User.class)));
    }

    @Test
    void recordAttendance_sameUserTwice_countsOnce() {
        Event e = new Workshop();
        e.setCapacity(5);
        User u = mock(User.class);

        e.recordAttendance(u);
        e.recordAttendance(u);

        assertEquals(4, e.getAvailableSeats());
    }

    @Test
    void getEventType_matchesClass() {
        assertEquals("Fundraiser", new Fundraiser().getEventType());
        assertEquals("Outreach", new Outreach().getEventType());
        assertEquals("Workshop", new Workshop().getEventType());
    }

    @Test
    void fundraiser_progressPercent() {
        Fundraiser f = new Fundraiser();
        f.setFundTarget(200);

        f.recordDonation(50);

        assertEquals(50.0, f.getAmountRaised(), 0.001);
        assertEquals(25.0, f.getProgressPercent(), 0.001);
    }

    @Test
    void fundraiser_reachingTarget_notifiesObservers() {
        Fundraiser f = new Fundraiser();
        f.setTitle("Drive");
        f.setFundTarget(100);
        Observer o = mock(Observer.class);
        f.attach(o);

        f.recordDonation(100);

        verify(o).update(f, "Fundraiser Drive reached its target!");
    }

    @Test
    void fundraiser_nonPositiveDonation_throws() {
        Fundraiser f = new Fundraiser();
        assertThrows(IllegalArgumentException.class, () -> f.recordDonation(0));
        assertThrows(IllegalArgumentException.class, () -> f.recordDonation(-5));
    }

    @Test
    void fundraiser_summary() {
        Fundraiser f = new Fundraiser();
        f.setTitle("Drive");
        f.setFundTarget(500);

        assertEquals("Fundraiser: Drive (SCHEDULED) - raised 0.0 of 500.0", f.getSummary());
    }

    @Test
    void workshop_reminder() {
        Workshop w = new Workshop();
        w.setTopic("Java");
        w.setInstructor("Sara");
        w.setStartDateTime(LocalDateTime.of(2026, 11, 10, 18, 0));

        assertEquals("Workshop reminder: Java with Sara starts at 2026-11-10T18:00",
                w.getReminderMessage());
    }

    @Test
    void outreach_addPartner() {
        Outreach o = new Outreach();
        o.addPartner("Food Bank");

        assertEquals(1, o.getPartnerList().size());
        assertEquals("Food Bank", o.getPartnerList().get(0));
    }
}
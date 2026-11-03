package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.*;
import com.tekkeyah.pattern.observer.Observer;
import com.tekkeyah.repository.EventRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

class EventCreatorTest {

    private final EventRepository repo = mock(EventRepository.class);

    @Test
    void eachCreator_returnsTheRightType() {
        assertInstanceOf(Fundraiser.class, new FundraiserCreator(repo).createEvent());
        assertInstanceOf(Outreach.class, new OutreachCreator(repo).createEvent());
        assertInstanceOf(Workshop.class, new WorkshopCreator(repo).createEvent());
    }

    @Test
    void scheduleEvent_savesTheEvent() {
        Event e = new FundraiserCreator(repo).scheduleEvent();

        assertInstanceOf(Fundraiser.class, e);
        verify(repo).save(e);
    }

    @Test
    void scheduleEvent_attachesObservers() {
        Observer o = mock(Observer.class);

        Event e = new WorkshopCreator(repo).scheduleEvent(o);
        e.setTitle("X");
        e.changeStatus(EventStatus.COMPLETED);

        verify(o).update(e, "Event X is now COMPLETED");
    }
}
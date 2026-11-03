package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Event;
import com.tekkeyah.pattern.observer.Observer;
import com.tekkeyah.repository.EventRepository;

public abstract class EventCreator {

    private final EventRepository eventRepository;

    protected EventCreator(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event scheduleEvent(Observer... observers) {
        Event e = createEvent();            // factory method: the subclass decides the type
        for (Observer o : observers) {
            e.attach(o);
        }
        save(e);
        return e;
    }

    public abstract Event createEvent();

    protected void save(Event e) {
        eventRepository.save(e);
    }
}
package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Event;
import com.tekkeyah.repository.EventRepository;

import java.util.Observer;

@SuppressWarnings("deprecation")
public abstract class EventCreator {

    private final EventRepository eventRepository;

    protected EventCreator(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event scheduleEvent(Observer... observers) {
        Event e = createEvent();            // factory method: the subclass decides the type
        if (observers != null) {
            for (Observer o : observers) {
                if (o != null) {
                    e.attach(o);
                }
            }
        }
        save(e);
        return e;
    }

    public abstract Event createEvent();

    protected void save(Event e) {
        eventRepository.save(e);
    }
}
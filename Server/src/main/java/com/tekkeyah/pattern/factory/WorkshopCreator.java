package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Event;
import com.tekkeyah.model.entity.Workshop;
import com.tekkeyah.repository.EventRepository;
import org.springframework.stereotype.Component;

@Component
public class WorkshopCreator extends EventCreator {

    public WorkshopCreator(EventRepository eventRepository) {
        super(eventRepository);
    }

    @Override
    public Event createEvent() {
        return new Workshop();
    }
}
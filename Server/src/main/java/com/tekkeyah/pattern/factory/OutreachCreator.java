package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Event;
import com.tekkeyah.model.entity.Outreach;
import com.tekkeyah.repository.EventRepository;
import org.springframework.stereotype.Component;

@Component
public class OutreachCreator extends EventCreator {

    public OutreachCreator(EventRepository eventRepository) {
        super(eventRepository);
    }

    @Override
    public Event createEvent() {
        return new Outreach();
    }
}
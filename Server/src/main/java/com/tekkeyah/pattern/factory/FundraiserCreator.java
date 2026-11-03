package com.tekkeyah.pattern.factory;

import com.tekkeyah.model.entity.Event;
import com.tekkeyah.model.entity.Fundraiser;
import com.tekkeyah.repository.EventRepository;
import org.springframework.stereotype.Component;

@Component
public class FundraiserCreator extends EventCreator {

    public FundraiserCreator(EventRepository eventRepository) {
        super(eventRepository);
    }

    @Override
    public Event createEvent() {
        return new Fundraiser();
    }
}
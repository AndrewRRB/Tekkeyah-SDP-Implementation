package com.tekkeyah.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Event{
    protected int eventID;
    protected String title;
    protected String description;
    protected LocalDateTime startDateTime;
    protected LocalDateTime endDateTime;
    protected int capacity;

    @ManyToMany
    protected List<User> attendees = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    protected EventStatus status = EventStatus.SCHEDULED;

    public Event(){

    }

    public int getEventID(){
        return eventID;
    }

    public String getTile(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(){
        this.description = description;
    }

    public LocalDateTime getStartDateTime(){
        return startDateTime;
    }

    public void setStartDateTime(){
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime(){
        return endDateTime;
    }

    public void setEndDateTime(){
        this.endDateTime = endDateTime;
    }

    public int getCapacity(){
        return capacity;
    }

    public void setCapacity(){
        this.capacity = capacity;
    }

    public EventStatus getStatus(){
        return status;
    }

    public void setStatus(){
        this.status = status;
    }

    public List<User> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<User> attendees) {
        this.attendees = attendees;
    }
}
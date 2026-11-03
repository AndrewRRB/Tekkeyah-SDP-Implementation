package com.tekkeyah.model.entity;
import com.tekkeyah.model.entity.EventStatus;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuppressWarnings("deprecation")
public abstract class Event extends Observable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Event(){}

    // Methods
    public int getAvailableSeats(){
        return capacity - attendees.size();
    }

    public void changeStatus(EventStatus newStatus){
        this.status = newStatus;
        notifyObservers("Event " + title + " is now " + newStatus);
    }

    public void recordAttendance(User attendee) {
        if (getAvailableSeats() <= 0) {
            throw new IllegalStateException("Event is full");
        }
        if (!attendees.contains(attendee)) {
            attendees.add(attendee);
        }
    }

    public abstract String getEventType();

    public String getSummary() {
        return getEventType() + ": " + title + " (" + status + ")";
    }

    public String getReminderMessage() {
        return "Reminder: " + title + " starts at " + startDateTime;
    }

    // Java's built-in Observer
    public void attach(Observer o) {
        this.addObserver(o);
    }

    public void detach(Observer o) {
        this.deleteObserver(o);
    }

    @Override
    public void notifyObservers(Object msg) {
        setChanged();
        super.notifyObservers(msg);
    }

    // Setters and Getters
    public int getEventID(){
        return eventID;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public LocalDateTime getStartDateTime(){
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime){
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime(){
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime){
        this.endDateTime = endDateTime;
    }

    public int getCapacity(){
        return capacity;
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public EventStatus getStatus(){
        return status;
    }

    public void setStatus(EventStatus status){
        this.status = status;
    }

    public List<User> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<User> attendees) {
        this.attendees = attendees;
    }
}
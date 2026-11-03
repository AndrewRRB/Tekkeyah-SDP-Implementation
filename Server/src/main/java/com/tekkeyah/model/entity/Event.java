package com.tekkeyah.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.tekkeyah.pattern.observer.Observer;
import com.tekkeyah.pattern.observer.Subject;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Event implements Subject{

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

    @Transient
    private List<Observer> observers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    protected EventStatus status = EventStatus.SCHEDULED;

    public Event(){}


    //methods
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




    //observer pattern
    @Override
    public void attach(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String msg) {
        for (Observer o : new ArrayList<>(observers)) {
            o.update(this, msg);
        }
    }


    //setters and getters
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
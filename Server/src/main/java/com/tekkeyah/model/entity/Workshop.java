package com.tekkeyah.model.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Workshop extends Event {

    private String topic;
    private String instructor;

    @ElementCollection
    private List<String> materials = new ArrayList<>();

    public Workshop() { }


    //methods
    @Override
    public String getEventType() {
        return "Workshop";
    }

    @Override
    public String getSummary() {
        return super.getSummary() + " topic: " + topic + ", by " + instructor;
    }

    @Override
    public String getReminderMessage() {
        return "Workshop reminder: " + topic + " with " + instructor
                + " starts at " + startDateTime;
    }



    //setters and getters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }
}
package com.tekkeyah.model.entity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Outreach extends Event{
    private String targetCommunity;

    @ElementCollection
    private List<String> partnerList = new ArrayList<>();

    public Outreach() { }


    //methods
    @Override
    public String getEventType() { return "Outreach"; }

    @Override
    public String getSummary() {
        return super.getSummary() + " - community: " + targetCommunity;
    }

    @Override
    public String getReminderMessage() {
        return "Outreach reminder: " + title + " for " + targetCommunity
                + " starts at " + startDateTime;
    }

    public void addPartner(String name) {
        partnerList.add(name);
    }

    public String getTargetCommunity() {
        return targetCommunity;
    }

    public void setTargetCommunity(String targetCommunity) {
        this.targetCommunity = targetCommunity;
    }

    public List<String> getPartnerList() {
        return partnerList;
    }

    public void setPartnerList(List<String> partnerList) {
        this.partnerList = partnerList;
    }
}

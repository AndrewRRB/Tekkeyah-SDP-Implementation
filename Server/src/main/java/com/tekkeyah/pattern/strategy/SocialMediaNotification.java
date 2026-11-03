package com.tekkeyah.pattern.strategy;

import com.tekkeyah.model.entity.User;
import org.springframework.stereotype.Component;

@Component("socialMediaNotification")
public class SocialMediaNotification implements NotificationStrategy {

    private String platform = "General Social Media";

    public SocialMediaNotification() {}

    public SocialMediaNotification(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public void send(User to, String msg) {
        System.out.println("[" + platform.toUpperCase() + " NOTIFICATION SENT] To User: "
                + to.getName() + " | Message: " + msg);
    }
}
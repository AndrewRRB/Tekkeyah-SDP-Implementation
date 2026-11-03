package com.tekkeyah.pattern.strategy;

import com.tekkeyah.model.entity.User;
import org.springframework.stereotype.Component;

@Component("emailNotification")
public class EmailNotification implements NotificationStrategy {

    @Override
    public void send(User to, String msg) {
        System.out.println("[EMAIL SENT] To: " + to.getEmail() + " | Message: " + msg);
    }
}
package com.tekkeyah.pattern.strategy;
import org.springframework.stereotype.Component;

import com.tekkeyah.model.entity.User;

@Component("emailNotification")
public class EmailNotification implements NotificationStrategy {

    @Override
    public void send(User to, String msg) {
        System.out.println("[EMAIL SENT] To: " + to.getEmail() + " | Message: " + msg);
    }
}
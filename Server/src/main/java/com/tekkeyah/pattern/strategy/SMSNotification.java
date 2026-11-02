package com.tekkeyah.pattern.strategy;

import com.tekkeyah.model.entities.User;
import org.springframework.stereotype.Component;

@Component("smsNotification")
public class SMSNotification implements NotificationStrategy {

    @Override
    public void send(User to, String msg) {
        System.out.println("[SMS SENT] To: " + to.getPhone() + " | Message: " + msg);
    }
}
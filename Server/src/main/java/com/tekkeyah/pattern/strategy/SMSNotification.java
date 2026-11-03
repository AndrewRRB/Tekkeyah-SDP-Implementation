package com.tekkeyah.pattern.strategy;

import org.springframework.stereotype.Component;

import com.tekkeyah.model.entity.User;

@Component("smsNotification")
public class SMSNotification implements NotificationStrategy {

    @Override
    public void send(User to, String msg) {
        System.out.println("[SMS SENT] To: " + to.getPhone() + " | Message: " + msg);
    }
}
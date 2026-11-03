package com.tekkeyah.pattern.strategy;

import org.springframework.stereotype.Component;

import com.tekkeyah.model.entity.User;

@Component("whatsAppNotification")
public class WhatsAppNotification implements NotificationStrategy {

    @Override
    public void send(User to, String msg) {
        System.out.println("[WHATSAPP SENT] To: " + to.getPhone() + " | Message: " + msg);
    }
}
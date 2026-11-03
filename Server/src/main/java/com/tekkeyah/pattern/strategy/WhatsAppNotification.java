package com.tekkeyah.pattern.strategy;

import com.tekkeyah.model.entity.User;
import org.springframework.stereotype.Component;

@Component("whatsAppNotification")
public class WhatsAppNotification implements NotificationStrategy {

    @Override
    public void send(User to, String msg) {
        System.out.println("[WHATSAPP SENT] To: " + to.getPhone() + " | Message: " + msg);
    }
}
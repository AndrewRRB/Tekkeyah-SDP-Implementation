package com.tekkeyah.pattern.strategy;

import com.tekkeyah.model.entities.User;

public interface NotificationStrategy {
    void send(User to, String msg);
}
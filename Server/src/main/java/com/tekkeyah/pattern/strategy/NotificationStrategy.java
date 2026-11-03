package com.tekkeyah.pattern.strategy;

import com.tekkeyah.model.entity.User;

public interface NotificationStrategy {
    void send(User to, String msg);
}
package com.tekkeyah.model.entities;

import com.tekkeyah.pattern.observer.Observer;
import com.tekkeyah.pattern.observer.Subject;
import com.tekkeyah.pattern.strategy.NotificationStrategy;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements Observer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    protected int userID;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "email", nullable = false, unique = true)
    protected String email;

    @Column(name = "phone")
    protected String phone;

    @Transient
    protected NotificationStrategy strategy;

    public User() {}

    public User(String name, String email, String phone, NotificationStrategy strategy) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.strategy = strategy;
    }

    // Dynamic Strategy Switcher
    public void setStrategy(NotificationStrategy s) {
        this.strategy = s;
    }

    public NotificationStrategy getStrategy() {
        return strategy;
    }

    // Sends notification using current strategy
    public void notify(String msg) {
        if (strategy != null) {
            strategy.send(this, msg);
        } else {
            System.out.println("[WARNING] No notification strategy set for user: " + name);
        }
    }

    // Implementation of Observer interface
    @Override
    public void update(Subject source, String msg) {
        notify(msg);
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
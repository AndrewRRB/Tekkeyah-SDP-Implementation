package com.tekkeyah.pattern.observer;

public interface Subject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers(String msg);
}
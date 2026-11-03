package com.tekkeyah.pattern.observer;

public interface Observer {
    void update(Subject source, String msg);
}
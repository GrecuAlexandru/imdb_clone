package org.example;

import java.util.ArrayList;
import java.util.List;

public class Rating implements Subject{
    private String username;
    private int rating;
    private String comment;
    private List<Observer> observers;

    public Rating(String username, int rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
        observers = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Notification notification) {
        for (Observer o : observers) {
            o.update(notification);
        }
    }
}

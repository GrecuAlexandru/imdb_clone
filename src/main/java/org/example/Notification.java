package org.example;

public class Notification {
    private String toUsername;
    private String message;

    public Notification(String toUsername, String message) {
        this.toUsername = toUsername;
        this.message = message;
    }

    public String getToUsername() {
        return toUsername;
    }

    public String getMessage() {
        return message;
    }
}

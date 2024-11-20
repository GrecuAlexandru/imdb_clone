package org.example;

import java.time.LocalDateTime;

public class Request {
    // TODO: add mode
    private RequestTypes type;
    private LocalDateTime createdDate; // Data va fi formatată folosind DateTimeFormatter
    private String username;
    private String to;
    private String description;
    private String actorName = null;
    private String movieTitle = null;

    public Request(RequestTypes type, LocalDateTime createdDate, String username, String to, String description, String actorName, String movieTitle) {
        this.type = type;
        this.createdDate = createdDate;
        this.username = username;
        this.to = to;
        this.description = description;
        this.actorName = actorName;
        this.movieTitle = movieTitle;
    }

    public void displayInfo() {
        System.out.println("Request type: " + this.type);
        if (this.actorName != null) {
            System.out.println("Actor name: " + this.actorName);
        } else if (this.movieTitle != null) {
            System.out.println("Movie title: " + this.movieTitle);
        }
        System.out.println("Created date: " + this.createdDate);
        System.out.println("Username: " + this.username);
        System.out.println("To: " + this.to);
        System.out.println("Description: " + this.description);
    }

    public RequestTypes getRequestType() {
        return this.type;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public String getUsername() {
        return this.username;
    }

    public String getTo() {
        return this.to;
    }

    public String getDescription() {
        return this.description;
    }

    public String getActorName() {
        return this.actorName;
    }

    public String getMovieTitle() {
        return this.movieTitle;
    }
}
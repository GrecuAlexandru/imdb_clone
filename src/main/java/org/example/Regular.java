package org.example;

import org.example.expstrategy.calculateFromRequest;

import java.util.List;
import java.util.SortedSet;

public class Regular<T> extends User implements RequestsManager {
    private List<Request> userRequests;
    private List<Rating> ratings;
    public SortedSet<Comparable<Object>> contributions;

    public Regular(
            Information info,
            AccountType accountType,
            String username,
            Integer experience,
            List<String> notifications,
            SortedSet<Comparable<Object>> favorites,
            SortedSet<Comparable<Object>> contributions) {
        super(info, accountType, username, experience, notifications, favorites);
        this.contributions = contributions;
    }

    public void createRequest(Request request) {
        userRequests.add(request);
        if (request.getRequestType() == RequestTypes.ACTOR_ISSUE || request.getRequestType() == RequestTypes.MOVIE_ISSUE) {
            this.updateExperience(new calculateFromRequest());
        }
    }

    public void deleteRequest(Request request) {
        userRequests.remove(request);
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
    }
}

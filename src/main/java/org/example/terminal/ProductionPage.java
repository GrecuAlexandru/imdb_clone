package org.example.terminal;

import org.example.*;
import org.example.expstrategy.calculateFromRating;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProductionPage {
    public static void productionPage(int userIndex, Production production) throws FileNotFoundException {
        System.out.println("Production Page");
        System.out.println("Title: " + production.getTitle());
        System.out.println("Type: " + production.getType());
        System.out.println("Plot: " + production.getPlot());
        System.out.println("Release Year: " + production.getReleaseYear());

        if (production.getRatings() != null) {
            System.out.println("Average Rating: " + production.getAverageRating());
        } else {
            System.out.println("Average Rating: N/A");
        }

        if (production.getType() == ProductionType.Movie.toString()) {
            System.out.println("Duration: " + production.getDuration());
        } else {
            System.out.println("Seasons: ");
            for (Map.Entry<String, List<Episode>> entry : production.getSeasons().entrySet()) {
                System.out.println(entry.getKey());
                for (Episode episode : entry.getValue()) {
                    System.out.println("    " + episode.getEpisodeName() + " - " + episode.getDuration());
                }
            }
        }

        System.out.println("Actors: ");
        for (int i = 0; i < production.getActors().size(); i++) {
            System.out.println("    " + (i + 1) + ". " + production.getActors().get(i));
        }

        System.out.println("Directors: ");
        for (int i = 0; i < production.getDirectors().size(); i++) {
            System.out.println("    " + (i + 1) + ". " + production.getDirectors().get(i));
        }

        if (production.getRatings() != null) {
            System.out.println("Reviews: ");
            for (int i = 0; i < production.getRatings().size(); i++) {
                System.out.println("    " + (i + 1) + ". " + production.getRatings().get(i).getUsername()
                        + "(" + IMDB.getInstance().getAccounts().get(IMDB.getInstance().findUserIndex(production.getRatings().get(i).getUsername())).getExperience() + "): "
                + production.getRatings().get(i).getRating() + " - " + production.getRatings().get(i).getComment());
            }
        }

        System.out.println("Choose action:");

        if (IMDB.getInstance().getAccounts().get(userIndex).getFavorites().contains(production)) {
            System.out.println("    1) Remove from favorites");
        } else {
            System.out.println("    1) Add to favorites");
        }

        if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Regular) {
            // check if i already rated this production
            String username = IMDB.getInstance().getAccounts().get(userIndex).getUsername();
            boolean rated = false;
            if (production.getRatings() != null) {
                for (Rating rating : production.getRatings()) {
                    if (rating.getUsername().equals(username)) {
                        rated = true;
                        break;
                    }
                }
            }

            if (rated) {
                System.out.println("    2) Delete rating");
            } else {
                System.out.println("    2) Add rating");
            }
        }

        System.out.println("    3) Back");

        Scanner scanner = new Scanner(System.in);

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                if (IMDB.getInstance().getAccounts().get(userIndex).getFavorites().contains(production)) {
                    IMDB.getInstance().getAccounts().get(userIndex).removeFavorite((Comparable<?>) production);
                    System.out.println("Removed from favorites");
                } else {
                    IMDB.getInstance().getAccounts().get(userIndex).addFavorite((Comparable<?>) production);
                    System.out.println("Added to favorites");
                }
                productionPage(userIndex, production);
                break;
            case 2:
                if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Regular) {
                    // check if i already rated this production
                    String username = IMDB.getInstance().getAccounts().get(userIndex).getUsername();
                    boolean rated = false;
                    if (production.getRatings() != null) {
                        for (Rating rating : production.getRatings()) {
                            if (rating.getUsername().equals(username)) {
                                rated = true;
                                break;
                            }
                        }
                    }

                    if (rated) {
                        // delete rating
                        Rating userRating = null;
                        for (Rating rating : production.getRatings()) {
                            if (rating.getUsername().equals(username)) {
                                userRating = rating;
                                break;
                            }
                        }
                        if (userRating != null) {
                            userRating.removeObserver(IMDB.getInstance().getAccounts().get(userIndex));
                            production.deleteRating(userRating);
                            System.out.println("Rating deleted");
                        } else {
                            System.out.println("You have not rated this production yet");
                        }
                        System.out.println("Rating deleted");
                    } else {
                        // add rating
                        System.out.println("Enter rating (1-10):");
                        int rating = scanner.nextInt();
                        System.out.println("Enter comment:");
                        String comment = scanner.next();

                        try {
                            if (rating >= 0 && rating <= 10 && comment != null && !comment.isEmpty()) {
                                Rating newRating = new Rating(username, rating, comment);
                                // add observers
                                for (Rating rating1 : production.getRatings()) {
                                    // we get all other ratings and get the username of the people who rated
                                    String username1 = rating1.getUsername();
                                    // we find the user with that username
                                    int userIdx = IMDB.getInstance().findUserIndex(username1);
                                    newRating.addObserver(IMDB.getInstance().getAccounts().get(userIdx));
                                }
                                String message = username + " added a rating to " + production.getTitle() + "(You also rated this production)";
                                newRating.notifyObservers(new Notification(username, message));
                                newRating.addObserver(IMDB.getInstance().getAccounts().get(userIndex));
                                production.addRating(newRating);
                                IMDB.getInstance().getAccounts().get(userIndex).updateExperience(new calculateFromRating());
                                System.out.println("Rating added");
                            } else {
                                System.out.println("Invalid input. Please try again.");
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid input. Please try again.");
                        }
                    }
                }
                break;
            case 3:
                ViewProductions.viewProductions(userIndex);
                break;
        }

    }
}

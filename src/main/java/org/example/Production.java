package org.example;

import org.example.utils.JsonParser;

import java.util.List;
import java.util.Map;

public abstract class Production {
    public String title;
    public ProductionType type;
    public List<String> directors; // regizori
    public List<String> actors;
    public List<String> genres;
    public List<Rating> ratings;
    public String plot; // descrierea subiectului filmului
    public Double averageRating; // rating-ul mediu al filmului (nota filmului)
    public Integer releaseYear;

    public Production(String title,
                      ProductionType type,
                      List<String> directors,
                      List<String> actors,
                      List<String> genres,
                      List<Rating> ratings,
                      String plot,
                      Double averageRating,
                      Integer releaseYear) {
        this.title = title;
        this.type = type;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = plot;
        this.averageRating = averageRating;
        this.releaseYear = releaseYear;
    }

    public void displayInfo() {
        System.out.println("Title: " + this.title);
        System.out.println("Type: " + this.type);
        System.out.println("Directors: ");
        for (String director : this.directors) {
            System.out.println(director);
        }
        System.out.println("Actors: ");
        for (String actor : this.actors) {
            System.out.println(actor);
        }
        System.out.println("Genres: ");
        for (String genre : this.genres) {
            System.out.println(genre);
        }
        System.out.println("Plot: " + this.plot);
        System.out.println("Average rating: " + this.averageRating);
        System.out.println("Release year: " + this.releaseYear);
        System.out.println("Ratings: ");
        for (Rating rating : this.ratings) {
            System.out.println("Username: " + rating.getUsername());
            System.out.println("Rating: " + rating.getRating());
            System.out.println("Comment: " + rating.getComment());
        }
    }

    public int compareTo(Comparable<Object> production) {
        Production x = (Production) production;
        return this.title.compareTo(x.title);
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        if (type == ProductionType.Movie)
            return "Movie";
        else if (type == ProductionType.Series)
            return "Series";
        else
            return "null";
    }

    public String getPlot() {
        return plot;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getDuration() {
        return null;
    }

    public Map<String, List<Episode>> getSeasons() {
        return null;
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
        JsonParser.writeProductionsJson();
    }

    public void deleteRating(Rating rating) {
        ratings.remove(rating);
        JsonParser.writeProductionsJson();
    }
}

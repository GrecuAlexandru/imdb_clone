package org.example;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class Movie extends Production implements Comparable<Object>{
    public String duration; // durata filmului in minute

    public Movie(String title,
                 ProductionType type,
                 List<String> directors,
                 List<String> actors,
                 List<String> genres,
                 List<Rating> ratings,
                 String plot,
                 Double averageRating,
                 Integer releaseYear,
                 String duration) {
        super(title, type, directors, actors, genres, ratings, plot, averageRating, releaseYear);
        this.duration = duration;
    }

    @Override
    public void displayInfo() {
        System.out.println("Title: " + title);
        System.out.println("Directors: " + directors);
        System.out.println("Actors: " + actors);
        System.out.println("Genres: " + genres);
        System.out.println("Ratings: " + ratings);
        System.out.println("Description: " + plot);
        System.out.println("Rating: " + averageRating);
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Release Year: " + releaseYear);
        System.out.println("--------------------");
    }

    @Override
    public int compareTo(Object other) {
        if (getClass() == Movie.class && other.getClass() == Movie.class) {
            Movie x = (Movie) other;
            return this.title.compareTo(x.title);
        }
        return -1;
    }

    @Override
    public String getDuration() {
        return duration;
    }
}

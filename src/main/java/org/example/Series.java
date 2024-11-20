package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class Series extends Production implements Comparable<Object> {
    public Integer numSeasons;
    public Map<String, List<Episode>> seasons;


    public Series(String title,
                  ProductionType type,
                  List<String> directors,
                  List<String> actors,
                  List<String> genres,
                  List<Rating> ratings,
                  String plot,
                  Double averageRating,
                  Integer releaseYear,
                  Integer numSeasons,
                  Map<String, List<Episode>> seasons) {
        super(title, type, directors, actors, genres, ratings, plot, averageRating, releaseYear);
        this.numSeasons = numSeasons;
        this.seasons = seasons;
    }
    @Override
    public void displayInfo() {
        System.out.println("Title: " + title);
        System.out.println("Type: " + type);
        System.out.println("Directors: " + directors);
        System.out.println("Actors: " + actors);
        System.out.println("Genres: " + genres);
        System.out.println("Ratings: " + ratings);
        System.out.println("Plot: " + plot);
        System.out.println("Average Rating: " + averageRating);
        System.out.println("Release Year: " + releaseYear);
        System.out.println("Number of seasons: " + numSeasons);
        System.out.println("Seasons: " + seasons);
    }

    public Integer getNumSeasons() {
        return numSeasons;
    }

    @Override
    public int compareTo(Object other) {
        if (getClass() == Series.class && other.getClass() == Series.class) {
            Series x = (Series) other;
            return this.title.compareTo(x.title);
        }
        return 1;
    }

    @Override
    public  Map<String, List<Episode>> getSeasons() {
        return seasons;
    }
}

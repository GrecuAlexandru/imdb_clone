package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Actor implements Comparable<Object>{
    public String name;
    public List<Performance> performances;
    public String biography;

    public Actor(String name,
                 List<Performance> performances,
                 String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
    }

    public void displayInfo() {
        System.out.println("Actor: " + this.name);
        System.out.println("Biography: " + this.biography);
        System.out.println("Performances: ");
        for (Performance performance : this.performances) {
            System.out.println("Title: " + performance.title);
            System.out.println("Type: " + performance.type);
        }
    }

    @Override
    public int compareTo(Object other) {
        if (getClass() == Actor.class && other.getClass() == Actor.class) {
            Actor x = (Actor) other;
            return this.name.compareTo(x.name);
        }
        return 1;
    }

//    public <U> U getName() {
//        return (U) name;
//    }

    public String getName() {
        return name;
    }

    public Iterable<? extends Performance> getPerformances() {
        return performances;
    }

    public String getBiography() {
        return biography;
    }

    public static class Performance {
        private String title;
        private ProductionType type;


        public Performance(String title,
                           ProductionType type) {
            this.title = title;
            this.type = type;
        }

        public String getTitle() {
            return this.title;
        }

        public ProductionType getType() {
            return this.type;
        }
    }
}

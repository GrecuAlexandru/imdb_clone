package org.example;

public enum ProductionType {
    Movie, Series;

    public static ProductionType fromString(String typeString) {
        // Choose the appropriate logic for handling a single string
        return switch (typeString) {
            case "Movie" -> Movie;
            case "Sitcom", "Series" -> Series;
            default -> throw new IllegalArgumentException("Invalid type value: " + typeString);
        };
    }
}

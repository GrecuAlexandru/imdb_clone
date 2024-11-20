package org.example.utils;

public class DurationConverter {
    public static String convertDuration(String duration) {
        // from "142 minutes" to "2h 22min"
        int minutes = Integer.parseInt(duration.split(" ")[0]);
        int hours = minutes / 60;
        minutes = minutes % 60;
        return hours + "h " + minutes + "min";
    }
}

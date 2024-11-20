package org.example.utils;

import org.example.ProductionType;

public class StringToProductionType {
    public static ProductionType convert(String type) {
        switch (type) {
            case "Movie":
                return ProductionType.Movie;
            case "Sitcom", "Series":
                return ProductionType.Series;
            default:
                throw new IllegalArgumentException("Unsupported production type: " + type);
        }
    }
}

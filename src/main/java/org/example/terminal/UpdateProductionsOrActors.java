package org.example.terminal;

import org.example.*;

import java.io.FileNotFoundException;
import java.util.*;

public class UpdateProductionsOrActors {
    public static void updateProductionsOrActors(int userIndex) throws FileNotFoundException {
        System.out.println("Update productions or actors");
        System.out.println("Choose action:");
        System.out.println("    1) Update production");
        System.out.println("    2) Update actor");
        System.out.println("    3) Go back");

        Scanner scanner = new Scanner(System.in);

        String input = scanner.next();

        switch(input) {
            case "1":
                updateProduction(userIndex, scanner);
                updateProductionsOrActors(userIndex);
                break;
            case "2":
                updateActor(userIndex, scanner);
                updateProductionsOrActors(userIndex);
                break;
            case "3":
                Menu.menu(userIndex);
                break;
            default:
                System.out.println("Invalid input");
                updateProductionsOrActors(userIndex);
        }
    }

    private static void updateProduction(int userIndex, Scanner scanner) {
        System.out.println("Choose the production you want to update:");
        for (int i = 0; i < IMDB.getInstance().getProductions().size(); i++) {
            System.out.println("    " + (i + 1) + ") " + IMDB.getInstance().getProductions().get(i).getTitle());
        }

        String input = scanner.next();
        int productionToUpdateIndex = Integer.parseInt(input) - 1;
        Production productionToUpdate = IMDB.getInstance().getProductions().get(productionToUpdateIndex);

        System.out.println("Enter the new title for the production:");
        String newTitle = scanner.next();

        System.out.println("Enter the new plot for the production:");
        String newPlot = scanner.next();

        System.out.println("Enter the new release year for the production:");
        String newReleaseYear = scanner.next();

        System.out.println("Enter the new average rating for the production:");
        String newAverageRating = scanner.next();

        System.out.println("Enter the new directors for the production (separated by commas):");
        String newDirectors = scanner.next();
        List<String> newDirectorsArray = List.of(newDirectors.split(", "));

        System.out.println("Enter the new actors for the production (separated by commas):");
        String newActors = scanner.next();
        List<String> newActorsArray = List.of(newActors.split(", "));

        System.out.println("Enter the new genres for the production (separated by commas):");
        String newGenres = scanner.next();
        List<String> newGenresArray = List.of(newGenres.split(", "));

        if (productionToUpdate.getType() == ProductionType.Movie.toString()) {
            System.out.println("Enter the new duration for the production (in minutes):");
            String newDuration = scanner.next();
            String newDurationString = newDuration + " minutes";

            Movie newMovie = new Movie(
                    newTitle,
                    ProductionType.Movie,
                    newDirectorsArray,
                    newActorsArray,
                    newGenresArray,
                    productionToUpdate.getRatings(),
                    newPlot,
                    Double.parseDouble(newAverageRating),
                    Integer.parseInt(newReleaseYear),
                    newDurationString
            );

            // Update the production in the database
            IMDB.getInstance().updateProduction(productionToUpdate.getTitle(), newMovie);
        } else {
            System.out.println("Enter the new number of seasons for the production:");
            String newNumberOfSeasons = scanner.next();
            Map<String, List<Episode>> newSeasons = new HashMap<>();

            for (int i = 0; i < Integer.parseInt(newNumberOfSeasons); i++) {
                String seasonName = "Season " + (i + 1);
                System.out.println("Enter the new number of episodes for season " + (i + 1) + ":");
                String newNumberOfEpisodes = scanner.next();
                List<Episode> episodes = new ArrayList<>();

                for (int j = 0; j < Integer.parseInt(newNumberOfEpisodes); j++) {
                    System.out.println("Enter the name of episode " + (j + 1) + " of season " + (i + 1) + ":");
                    String episodeName = scanner.next();
                    System.out.println("Enter the new duration of episode " + (j + 1) + " of season " + (i + 1) + ":");
                    String newEpisodeDuration = scanner.next();
                    String newEpisodeDurationString = newEpisodeDuration + " minutes";
                    Episode episode = new Episode(episodeName, newEpisodeDurationString);
                    episodes.add(episode);
                }
                newSeasons.put(seasonName, episodes);
            }

            Series newSeries = new Series(
                    newTitle,
                    ProductionType.Series,
                    newDirectorsArray,
                    newActorsArray,
                    newGenresArray,
                    productionToUpdate.getRatings(),
                    newPlot,
                    Double.parseDouble(newAverageRating),
                    Integer.parseInt(newReleaseYear),
                    Integer.parseInt(newNumberOfSeasons),
                    newSeasons
            );

            // Update the production in the database
            IMDB.getInstance().updateProduction(productionToUpdate.getTitle(), newSeries);
        }
    }

    private static void updateActor(int userIndex, Scanner scanner) {
        System.out.println("Choose the actor you want to update:");
        for (int i = 0; i < IMDB.getInstance().getActors().size(); i++) {
            System.out.println("    " + (i + 1) + ") " + IMDB.getInstance().getActors().get(i).getName());
        }

        String input = scanner.next();
        int actorToUpdateIndex = Integer.parseInt(input) - 1;
        Actor actorToUpdate = IMDB.getInstance().getActors().get(actorToUpdateIndex);

        System.out.println("Enter the new name for the actor:");
        String newName = scanner.next();

        System.out.println("Enter the new biography for the actor:");
        String newBiography = scanner.next();

        System.out.println("Enter the new performances for the actor (separated by commas):");
        String newPerformances = scanner.next();
        List<String> newPerformancesArray = List.of(newPerformances.split(", "));
        List<Actor.Performance> performances = new ArrayList<>();
        for (String performance : newPerformancesArray) {
            String[] performanceArray = performance.split(" \\(");
            String title = performanceArray[0];
            String type = performanceArray[1].substring(0, performanceArray[1].length() - 1);
            Actor.Performance newPerformance = new Actor.Performance(title, ProductionType.fromString(type));
            performances.add(newPerformance);
        }

        Actor newActor = new Actor(
                newName,
                performances,
                newBiography
        );

        // Update the actor in the database
        IMDB.getInstance().updateActor(actorToUpdate.getName(), newActor);
    }
}

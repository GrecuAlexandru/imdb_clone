package org.example.terminal;

import org.example.*;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.*;

public class ManageProductionsOrActors {
    public static void manageProductionsOrActors(int userIndex) throws FileNotFoundException {
        System.out.println("Manage productions or actors");
        System.out.println("Choose action:");
        System.out.println("    1) Add production");
        System.out.println("    2) Delete production");
        System.out.println("    3) Add actor");
        System.out.println("    4) Delete actor");
        System.out.println("    5) Go back");

        Scanner scanner = new Scanner(System.in);

        String input = scanner.next();

        switch(input) {
            case "1":
                addProduction(userIndex, scanner);
                manageProductionsOrActors(userIndex);
                break;
            case "2":
                deleteProduction(userIndex, scanner);
                manageProductionsOrActors(userIndex);
                break;
            case "3":
                addActor(userIndex, scanner);
                manageProductionsOrActors(userIndex);
                break;
            case "4":
                deleteActor(userIndex, scanner);
                manageProductionsOrActors(userIndex);
                break;
            case "5":
                Menu.menu(userIndex);
                break;
            default:
                System.out.println("Invalid input");
                manageProductionsOrActors(userIndex);
        }
    }

    private static void addProduction(int userIndex, Scanner scanner) {
        System.out.println("Enter the title of the production you want to add (or type x to go back):");
        String title = scanner.next();

        if (title.equals("x")) {
            addProduction(userIndex, scanner);
        } else {
            System.out.println("Enter the release year of the production:");
            Integer releaseYear = Integer.parseInt(scanner.next());

            List<String> genres = new ArrayList<>();
            System.out.println("Enter a genre of the production (or type x to finish):");
            String genre = scanner.next();
            while (!genre.equals("x")) {
                genres.add(genre);
                System.out.println("Enter a genre of the production (or type x to finish):");
                genre = scanner.next();
            }

            System.out.println("Enter the plot of the production:");
            String plot = scanner.next();

            List<String> directors = new ArrayList<>();
            System.out.println("Enter a director of the production (or type x to finish):");
            String director = scanner.next();
            while (!director.equals("x")) {
                directors.add(director);
                System.out.println("Enter a director of the production (or type x to finish):");
                director = scanner.next();
            }

            List<String> actors = new ArrayList<>();
            System.out.println("Enter an actor of the production (or type x to finish):");
            String actor = scanner.next();
            while (!actor.equals("x")) {
                actors.add(actor);
                System.out.println("Enter an actor of the production (or type x to finish):");
                actor = scanner.next();
            }

            System.out.println("Enter the average rating of the production:");
            double averageRating = Double.parseDouble(scanner.next());

            System.out.println("Enter the type of the production (movie or series):");
            String type = scanner.next();
            ProductionType productionType = ProductionType.fromString(type);

            if (productionType == ProductionType.Movie) {
                System.out.println("Enter the duration of the production (in minutes):");
                int duration = Integer.parseInt(scanner.next());
                String durationString = duration + " minutes";

                Movie newMovie = new Movie(
                        title,
                        productionType,
                        directors,
                        actors,
                        genres,
                        null,
                        plot,
                        averageRating,
                        releaseYear,
                        durationString
                );

                if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
                    Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
                    admin.addProductionSystem(newMovie);
                } else {
                    Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(userIndex);
                    contributor.addProductionSystem(newMovie);
                }
            } else if (productionType == ProductionType.Series) {
                // Similar code for adding a series
                System.out.println("Enter the number of seasons of the production:");
                Integer numberOfSeasons = Integer.parseInt(scanner.next());
                Map<String, List<Episode>> seasons = new HashMap<>();
                for (int i = 0; i < numberOfSeasons; i++) {
                    String seasonName = "Season " + (i + 1);
                    System.out.println("Enter the number of episodes of season " + (i + 1) + ":");
                    Integer numberOfEpisodes = Integer.parseInt(scanner.next());
                    List<Episode> episodes = new ArrayList<>();
                    for (int j = 0; j < numberOfEpisodes; j++) {
                        System.out.println("Enter the name of episode " + (j + 1) + " of season " + (i + 1) + ":");
                        String episodeName = scanner.next();
                        System.out.println("Enter the duration of episode " + (j + 1) + " of season " + (i + 1) + " (in minutes):");
                        int episodeDuration = Integer.parseInt(scanner.next());
                        String episodeDurationString = episodeDuration + " minutes";
                        Episode episode = new Episode(episodeName, episodeDurationString);
                        episodes.add(episode);
                    }
                    seasons.put(seasonName, episodes);
                }
                Series newSeries = new Series(
                        title,
                        productionType,
                        directors,
                        actors,
                        genres,
                        null,
                        plot,
                        averageRating,
                        releaseYear,
                        numberOfSeasons,
                        seasons
                );
                if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
                    Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
                    admin.addProductionSystem(newSeries);
                } else {
                    Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(userIndex);
                    contributor.addProductionSystem(newSeries);
                }
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
        System.out.println("Production added");
    }

    private static void deleteProduction(int userIndex, Scanner scanner) {
        System.out.println("Enter the title of the production you want to delete (or type x to go back):");
        String title = scanner.next();

        if (title.equals("x")) {
            deleteProduction(userIndex, scanner);
        } else {
            Production productionToDelete = null;
            for (Production production : IMDB.getInstance().getProductions()) {
                if (production.getTitle().equals(title)) {
                    productionToDelete = production;
                    break;
                }
            }

            if (productionToDelete == null) {
                System.out.println("Production not found");
                deleteProduction(userIndex, scanner);
            } else {
                if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
                    Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
                    admin.removeProductionSystem(productionToDelete.getTitle());
                } else {
                    Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(userIndex);
                    contributor.removeProductionSystem(productionToDelete.getTitle());
                }
            }
        }
        System.out.println("Production deleted");
    }

    private static void addActor(int userIndex, Scanner scanner) {
        System.out.println("Enter the name of the actor you want to add (or type x to go back):");
        String name = scanner.next();
        System.out.println("Enter the biography of the actor:");
        String biography = scanner.next();
        List<Actor.Performance> performances = new ArrayList<>();
        System.out.println("Enter the number of performances of the actor:");
        int numPerformances = Integer.parseInt(scanner.next());
        for (int i = 0; i < numPerformances; i++) {
            System.out.println("Enter the title of a performance of the actor:");
            String title = scanner.next();
            System.out.println("Enter the type of the performance (movie or series):");
            String type = scanner.next();
            ProductionType productionType = ProductionType.fromString(type);
            performances.add(new Actor.Performance(title, productionType));
        }
        Actor newActor = new Actor(name, performances, biography);
        if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
            Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
            admin.addActorSystem(newActor);
        } else {
            Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(userIndex);
            contributor.addActorSystem(newActor);
        }
        System.out.println("Actor added");
    }

    private static void deleteActor(int userIndex, Scanner scanner) throws FileNotFoundException {
        System.out.println("Enter the name of the actor you want to delete (or type x to go back):");
        String name = scanner.next();

        if (name.equals("x")) {
            manageProductionsOrActors(userIndex);
        } else {
            Actor actorToDelete = null;
            for (Actor actor : IMDB.getInstance().getActors()) {
                if (actor.getName().equals(name)) {
                    actorToDelete = actor;
                    break;
                }
            }

            if (actorToDelete == null) {
                System.out.println("Actor not found");
                deleteActor(userIndex, scanner);
            } else {
                if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
                    Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
                    admin.removeActorSystem(actorToDelete.getName());
                } else {
                    Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(userIndex);
                    contributor.removeActorSystem(actorToDelete.getName());
                }
            }
        }
        System.out.println("Actor deleted");
    }
}

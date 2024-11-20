package org.example.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.*;
import org.example.gui.FavoritesPanelGUI;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class JsonParser {
    public static List<Production> loadProductionsFromFile(String path) throws FileNotFoundException {
        List<Production> productions = null;
        JSONParser parser = new JSONParser();
        Reader reader = new FileReader(path);
        try {
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                JSONArray productionsArray = (JSONArray) obj;
                productions = new ArrayList<>();
                for (Object productionObj : productionsArray) {
                    JSONObject productionJson = (JSONObject) productionObj;
                    String title = (String) productionJson.get("title");
                    ProductionType type = StringToProductionType.convert((String) productionJson.get("type"));
                    List<String> directors = (List<String>) productionJson.get("directors");
                    List<String> actors = (List<String>) productionJson.get("actors");
                    List<String> genres = (List<String>) productionJson.get("genres");
                    List<Rating> ratings = null;
                    if (productionJson.containsKey("ratings")) {
                        ratings = new ArrayList<>();
                        JSONArray ratingsArray = (JSONArray) productionJson.get("ratings");
                        for (Object ratingObj : ratingsArray) {
                            JSONObject ratingJson = (JSONObject) ratingObj;
                            String username = (String) ratingJson.get("username");
                            Number ratingLong = (Number) ratingJson.get("rating");
                            Integer rating = ratingLong.intValue();
                            String comment = (String) ratingJson.get("comment");
                            ratings.add(new Rating(username, rating, comment));
                        }
                    }
                    String plot = (String) productionJson.get("plot");
                    Number averageRatingLong = (Number) productionJson.get("averageRating");
                    Double averageRating = averageRatingLong.doubleValue();
                    Integer releaseYear;
                    if (productionJson.get("releaseYear") != null) {
                        releaseYear = ((Number) productionJson.get("releaseYear")).intValue();
                    } else {
                        releaseYear = null;
                    }
                    if (type == ProductionType.Movie) {
                        String duration = (String) productionJson.get("duration");
                        productions.add(new Movie(title, type, directors, actors, genres, ratings, plot, averageRating, releaseYear, duration));
                    } else if (type == ProductionType.Series) {
                        Integer numSeasons = ((Long) productionJson.get("numSeasons")).intValue();
                        JSONObject seasonsObj = (JSONObject) productionJson.get("seasons");
                        Map<String, List<Episode>> seasons = new HashMap<>();
                        for (Object keyObj : seasonsObj.keySet()) {
                            String season = (String) keyObj;
                            JSONArray episodesArray = (JSONArray) seasonsObj.get(season);
                            List<Episode> episodes = new ArrayList<>();
                            for (Object episodeObj : episodesArray) {
                                JSONObject episodeJson = (JSONObject) episodeObj;
                                String episodeName = (String) episodeJson.get("episodeName");
                                String duration = (String) episodeJson.get("duration");
                                Episode episode = new Episode(episodeName, duration);
                                episodes.add(episode);
                            }
                            seasons.put(season, episodes);
                        }
                        productions.add(new Series(title, type, directors, actors, genres, ratings, plot, averageRating, releaseYear, numSeasons, seasons));
                    }
                }
            } else {
                System.out.println("Error: root is not an object");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productions;
    }

    public static List<Actor> loadActorsFromFile(String path) {
        List<Actor> actors = null;
        JSONParser parser = new JSONParser();
        try {
            Reader reader = new FileReader(path);
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                JSONArray actorsArray = (JSONArray) obj;
                actors = new ArrayList<>();
                for (Object actorObj : actorsArray) {
                    JSONObject actorJson = (JSONObject) actorObj;
                    String name = (String) actorJson.get("name");
                    List<Actor.Performance> performances = new ArrayList<>();
                    JSONArray performancesArray = (JSONArray) actorJson.get("performances");
                    for (Object performanceObj : performancesArray) {
                        JSONObject performanceJson = (JSONObject) performanceObj;
                        String title = (String) performanceJson.get("title");
                        String type = (String) performanceJson.get("type");
                        ProductionType productionType = StringToProductionType.convert(type);
                        performances.add(new Actor.Performance(title, productionType));
                    }
                    String biography = (String) actorJson.get("biography");
                    actors.add(new Actor(name, performances, biography));
                }
            } else {
                System.out.println("Error: root is not an object");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actors;
    }

    public static List<User> loadAccountsFromFile(String path) {
        List<User> accounts = null;
        JSONParser parser = new JSONParser();
        try {
            Reader reader = new FileReader(path);
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                JSONArray accountsArray = (JSONArray) obj;
                accounts = new ArrayList<>();
                for (Object accountObj : accountsArray) {
                    JSONObject accountJson = (JSONObject) accountObj;
                    String username = (String) accountJson.get("username");
                    Object experienceNode = accountJson.get("experience");
                    Integer experience;
                    if (experienceNode == null) {
                        experience = 0;
                    } else if (experienceNode instanceof Long) {
                        experience = ((Number) experienceNode).intValue();
                    } else {
                        String exp = (String) experienceNode;
                        experience = Integer.parseInt(exp);
                    }
                    JSONObject information = (JSONObject) accountJson.get("information");
                    JSONObject credentials = (JSONObject) information.get("credentials");
                    String email = (String) credentials.get("email");
                    String password = (String) credentials.get("password");
                    Credentials cred = new Credentials(email, password);
                    String name = (String) information.get("name");
                    String country = (String) information.get("country");
                    Integer age = ((Long) information.get("age")).intValue();
                    String gender = (String) information.get("gender");
                    String bdy = (String) information.get("birthDate");
                    LocalDateTime birthDate;
                    try {
                        birthDate = LocalDate.parse(bdy).atStartOfDay();
                    } catch (Exception e) {
                        birthDate = LocalDateTime.parse(bdy);
                    }
                    User.Information info = new User.Information.InformationBuilder(cred)
                            .name(name)
                            .country(country)
                            .age(age)
                            .gender(gender)
                            .birthDate(birthDate)
                            .build();
                    String userType = (String) accountJson.get("userType");
                    List<String> favoriteProductions = null;
                    if (accountJson.containsKey("favoriteProductions")) {
                        favoriteProductions = (List<String>) accountJson.get("favoriteProductions");
                    }

                    List<String> favoriteActors = null;
                    if (accountJson.containsKey("favoriteActors")) {
                        favoriteActors = (List<String>) accountJson.get("favoriteActors");
                    }

                    SortedSet<Comparable<?>> favorites = new TreeSet<>();
                    if (favoriteProductions != null) {
                        for (String favoriteProduction : favoriteProductions) {
                            Production production = IMDB.getInstance().findProduction(favoriteProduction);
                            if (production != null) {
                                favorites.add((Comparable<?>) production);
                            }
                        }
                    }

                    if (favoriteActors != null) {
                        for (String favoriteActor : favoriteActors) {
                            Actor actor = IMDB.getInstance().findActor(favoriteActor);
                            if (actor != null) {
                                favorites.add(actor);
                            }
                        }
                    }

                    List<String> notifications = null;
                    if (accountJson.containsKey("notifications")) {
                        notifications = (List<String>) accountJson.get("notifications");
                    }

                    if (userType.equals("Regular")) {
                        accounts.add(UserFactory.createUser(AccountType.Regular,
                                info,
                                username,
                                experience,
                                notifications,
                                favorites,
                                null));
                    } else if (userType.equals("Admin")) {
                        SortedSet<Comparable<?>> contributions = new TreeSet<>();
                        List<String> productionsContribution = null;
                        if (accountJson.containsKey("productionsContribution")) {
                            productionsContribution = (List<String>) accountJson.get("productionsContribution");
                        }
                        List<String> actorsContribution = null;
                        if (accountJson.containsKey("actorsContribution")) {
                            actorsContribution = (List<String>) accountJson.get("actorsContribution");
                        }
                        if (productionsContribution != null) {
                            for (String productionContribution : productionsContribution) {
                                Production production = IMDB.getInstance().findProduction(productionContribution);
                                if (production != null) {
                                    contributions.add((Comparable<?>) production);

                                }
                            }
                        }

                        if (actorsContribution != null) {
                            for (String actorContribution : actorsContribution) {
                                Actor actor = IMDB.getInstance().findActor(actorContribution);
                                if (actor != null) {
                                    contributions.add(actor);
                                }
                            }
                        }


                        System.out.println(username);
                        System.out.println(contributions);

                        accounts.add(UserFactory.createUser(AccountType.Admin,
                                info,
                                username,
                                experience,
                                notifications,
                                favorites,
                                contributions));
                    } else if (userType.equals("Contributor")) {
                        SortedSet<Comparable<?>> contributions = new TreeSet<>();
                        List<String> productionsContribution = null;
                        if (accountJson.containsKey("productionsContribution")) {
                            productionsContribution = (List<String>) accountJson.get("productionsContribution");
                        }
                        List<String> actorsContribution = null;
                        if (accountJson.containsKey("actorsContribution")) {
                            actorsContribution = (List<String>) accountJson.get("actorsContribution");
                        }
                        if (productionsContribution != null) {
                            for (String productionContribution : productionsContribution) {
                                Production production = IMDB.getInstance().findProduction(productionContribution);
                                if (production != null) {
                                    contributions.add((Comparable<?>) production);

                                }
                            }
                        }

                        if (actorsContribution != null) {
                            for (String actorContribution : actorsContribution) {
                                Actor actor = IMDB.getInstance().findActor(actorContribution);
                                if (actor != null) {
                                    contributions.add(actor);
                                }
                            }
                        }

                        System.out.println(username);
                        System.out.println(contributions);

                        accounts.add(UserFactory.createUser(AccountType.Contributor,
                                info,
                                username,
                                experience,
                                notifications,
                                favorites,
                                contributions));
                    }
                }
            } else {
                System.out.println("Error: root is not an object");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public static void writeAccountsJson(List<User> accounts) {
        String path = "src/main/resources/input/accounts.json";
        JSONArray accountsArray = new JSONArray();
        for (User user : accounts) {
            JSONObject userJson = new JSONObject();
            userJson.put("username", user.getUsername());
            userJson.put("experience", user.getExperience());
            userJson.put("userType", user.getType().toString());

            JSONObject infoJson = new JSONObject();
            infoJson.put("country", user.getCountry());
            infoJson.put("gender", user.getGender());
            infoJson.put("name", user.getName());
            infoJson.put("age", user.getAge());
            infoJson.put("birthDate", user.getBirthDate().toString());

            JSONObject credentialsJson = new JSONObject();
            credentialsJson.put("email", user.getEmail());
            credentialsJson.put("password", user.getPassword());
            infoJson.put("credentials", credentialsJson);

            userJson.put("information", infoJson);

            if (user.favorites != null) {
                JSONArray favoriteProductionsArray = new JSONArray();
                JSONArray favoriteActorsArray = new JSONArray();
                for (Object favorite : user.getFavorites()) {
                    if (favorite instanceof Production) {
                        favoriteProductionsArray.add(((Production) favorite).getTitle());
                    } else {
                        favoriteActorsArray.add(((Actor) favorite).getName());
                    }
                }
                if (favoriteProductionsArray.size() > 0)
                    userJson.put("favoriteProductions", favoriteProductionsArray);
                if (favoriteActorsArray.size() > 0)
                    userJson.put("favoriteActors", favoriteActorsArray);
            }


            if (user.getNotifications() != null) {
                JSONArray notificationsArray = new JSONArray();
                for (int i = 0; i < user.getNotifications().size(); i++) {
                    notificationsArray.add(user.getNotifications().get(i));
                }
                userJson.put("notifications", notificationsArray);
            }


            if (user.getType() == AccountType.Admin) {
                Admin u = (Admin) user;
                if (u.contributions != null) {
                    JSONArray productionsContribution = new JSONArray();
                    JSONArray actorsContribution = new JSONArray();
                    for (Object contribution : u.getContributions()) {
                        if (contribution instanceof Production) {
                            productionsContribution.add(((Production) contribution).getTitle());
                        } else {
                            actorsContribution.add(((Actor) contribution).getName());
                        }
                    }
                    if (productionsContribution.size() > 0) {
                        userJson.put("productionsContribution", productionsContribution);
                    }

                    if (actorsContribution.size() > 0) {
                        userJson.put("actorsContribution", actorsContribution);
                    }
                }
            } else if (user.getType() == AccountType.Contributor) {
                Contributor u = (Contributor) user;
                if (u.contributions != null) {
                    JSONArray productionsContributionArray = new JSONArray();
                    JSONArray actorsContribution = new JSONArray();
                    for (Object contribution : u.getContributions()) {
                        if (contribution instanceof Production) {
                            productionsContributionArray.add(((Production) contribution).getTitle());
                        } else {
                            actorsContribution.add(((Actor) contribution).getName());
                        }
                    }
                    if (productionsContributionArray.size() > 0) {
                        userJson.put("productionsContribution", productionsContributionArray);
                    }

                    if (actorsContribution.size() > 0) {
                        userJson.put("actorsContribution", actorsContribution);
                    }
                }
            }

            accountsArray.add(userJson);
        }

        try (FileWriter file = new FileWriter(path)) {
            file.write(accountsArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Request> loadRequestsFromFile(String path) {
        List<Request> requests = null;
        JSONParser parser = new JSONParser();
        try {
            Reader reader = new FileReader(path);
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                JSONArray requestsArray = (JSONArray) obj;
                requests = new ArrayList<>();
                for (Object requestObj : requestsArray) {
                    JSONObject requestJson = (JSONObject) requestObj;
                    RequestTypes type = RequestTypes.valueOf((String) requestJson.get("type"));
                    LocalDateTime createdDate = LocalDateTime.parse((String) requestJson.get("createdDate"));
                    String username = (String) requestJson.get("username");
                    String to = (String) requestJson.get("to");
                    String description = (String) requestJson.get("description");
                    String actorName = null;
                    String movieTitle = null;
                    if (requestJson.containsKey("actorName")) {
                        actorName = (String) requestJson.get("actorName");
                    }
                    if (requestJson.containsKey("movieTitle")) {
                        movieTitle = (String) requestJson.get("movieTitle");
                    }
                    requests.add(new Request(type, createdDate, username, to, description, actorName, movieTitle));
                }
            } else {
                System.out.println("Error: root is not an object");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static void addFavoritesToJson(int userIndex, Comparable<?> obj) {
        String path = "src/main/resources/input/accounts.json";
        JSONParser parser = new JSONParser();
        if (obj instanceof Production) {
            Production prod = (Production) obj;
            try {
                // Parse the JSON file
                Object fileObj = parser.parse(new FileReader(path));
                JSONArray accounts = (JSONArray) fileObj;

                // Get the user at the specified index
                JSONObject user = (JSONObject) accounts.get(userIndex);

                // Get the favoriteProductions array from the user
                JSONArray favoriteProductions = (JSONArray) user.get("favoriteProductions");

                if (favoriteProductions == null) {
                    favoriteProductions = new JSONArray();
                }

                // Add the new production to the favoriteProductions array
                favoriteProductions.add(prod.getTitle());

                // Update the JSON object with the modified favoriteProductions array
                user.put("favoriteProductions", favoriteProductions);
                // Write the updated JSON object back to the file
                try (FileWriter fileWriter = new FileWriter(path)) {
                    fileWriter.write(accounts.toJSONString());
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Production added to favorites successfully.");

            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
        } else {
            Actor actor = (Actor) obj;
            try {
                // Parse the JSON file
                Object fileObj = parser.parse(new FileReader(path));
                JSONArray accounts = (JSONArray) fileObj;

                // Get the user at the specified index
                JSONObject user = (JSONObject) accounts.get(userIndex);

                // Get the favoriteActors array from the user
                JSONArray favoriteActors = (JSONArray) user.get("favoriteActors");

                if (favoriteActors == null) {
                    favoriteActors = new JSONArray();
                }
                // Add the new actor to the favoriteActors array
                favoriteActors.add(actor.getName());

                // Update the JSON object with the modified favoriteActors array
                user.put("favoriteActors", favoriteActors);

                // Write the updated JSON object back to the file
                try (FileWriter fileWriter = new FileWriter(path)) {
                    fileWriter.write(accounts.toJSONString());
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Actor added to favorites successfully.");

            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeFavoritesFromJson(int userIndex, Comparable<?> obj) {
        String path = "src/main/resources/input/accounts.json";
        JSONParser parser = new JSONParser();
        if (obj instanceof Production) {
            Production prod = (Production) obj;
            try {
                // Parse the JSON file
                Object fileObj = parser.parse(new FileReader(path));
                JSONArray accounts = (JSONArray) fileObj;

                // Get the user at the specified index
                JSONObject user = (JSONObject) accounts.get(userIndex);

                // Get the favoriteProductions array from the user
                JSONArray favoriteProductions = (JSONArray) user.get("favoriteProductions");

                // Remove the production from the favoriteProductions array
                favoriteProductions.remove(prod.getTitle());

                // Update the JSON object with the modified favoriteProductions array
                user.put("favoriteProductions", favoriteProductions);

                // Write the updated JSON object back to the file
                try (FileWriter fileWriter = new FileWriter(path)) {
                    fileWriter.write(accounts.toJSONString());
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Production removed from favorites successfully.");

            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
        } else {
            Actor actor = (Actor) obj;
            try {
                // Parse the JSON file
                Object fileObj = parser.parse(new FileReader(path));
                JSONArray accounts = (JSONArray) fileObj;

                // Get the user at the specified index
                JSONObject user = (JSONObject) accounts.get(userIndex);

                // Get the favoriteActors array from the user
                JSONArray favoriteActors = (JSONArray) user.get("favoriteActors");

                // Remove the actor from the favoriteActors array
                favoriteActors.remove(actor.getName());

                // Update the JSON object with the modified favoriteActors array
                user.put("favoriteActors", favoriteActors);

                // Write the updated JSON object back to the file
                try (FileWriter fileWriter = new FileWriter(path)) {
                    fileWriter.write(accounts.toJSONString());
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Actor removed from favorites successfully.");

            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeRequestsJson() {
        String path = "src/main/resources/input/requests.json";
        JSONArray requestsArray = new JSONArray();
        for (Request request : IMDB.getInstance().getRequests()) {
            JSONObject requestJson = new JSONObject();
            requestJson.put("type", request.getRequestType().toString());
            requestJson.put("createdDate", request.getCreatedDate().toString());
            requestJson.put("username", request.getUsername());
            requestJson.put("to", request.getTo());
            requestJson.put("description", request.getDescription());
            if (request.getActorName() != null) {
                requestJson.put("actorName", request.getActorName());
            }
            if (request.getMovieTitle() != null) {
                requestJson.put("movieTitle", request.getMovieTitle());
            }
            requestsArray.add(requestJson);
        }
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(requestsArray.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeProductionsJson() {
        String path = "src/main/resources/input/production.json";
        JSONArray productionsArray = new JSONArray();
        for (Production production : IMDB.getInstance().getProductions()) {
            JSONObject productionJson = new JSONObject();
            productionJson.put("title", production.getTitle());
            productionJson.put("type", production.getType().toString());
            productionJson.put("directors", production.getDirectors());
            productionJson.put("actors", production.getActors());
            productionJson.put("genres", production.getGenres());
            if (production.getRatings() != null) {
                JSONArray ratingsArray = new JSONArray();
                for (Rating rating : production.getRatings()) {
                    JSONObject ratingJson = new JSONObject();
                    ratingJson.put("username", rating.getUsername());
                    ratingJson.put("rating", rating.getRating());
                    ratingJson.put("comment", rating.getComment());
                    ratingsArray.add(ratingJson);
                }
                productionJson.put("ratings", ratingsArray);
            }
            productionJson.put("plot", production.getPlot());
            productionJson.put("averageRating", production.getAverageRating());
            productionJson.put("releaseYear", production.getReleaseYear());
            if (ProductionType.fromString(production.getType()) == ProductionType.Movie) {
                Movie movie = (Movie) production;
                productionJson.put("duration", movie.getDuration());
            } else if (ProductionType.fromString(production.getType()) == ProductionType.Series) {
                Series series = (Series) production;
                productionJson.put("numSeasons", series.getNumSeasons());
                JSONObject seasonsJson = new JSONObject();
                for (String season : series.getSeasons().keySet()) {
                    JSONArray episodesArray = new JSONArray();
                    for (Episode episode : series.getSeasons().get(season)) {
                        JSONObject episodeJson = new JSONObject();
                        episodeJson.put("episodeName", episode.getEpisodeName());
                        episodeJson.put("duration", episode.getDuration());
                        episodesArray.add(episodeJson);
                    }
                    seasonsJson.put(season, episodesArray);
                }
                productionJson.put("seasons", seasonsJson);
            }
            productionsArray.add(productionJson);
        }
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(productionsArray.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeActorsJson() {
        String path = "src/main/resources/input/actors.json";
        JSONArray actorsArray = new JSONArray();
        for (Actor actor : IMDB.getInstance().getActors()) {
            JSONObject actorJson = new JSONObject();
            actorJson.put("name", actor.getName());
            JSONArray performancesArray = new JSONArray();
            for (Actor.Performance performance : actor.getPerformances()) {
                JSONObject performanceJson = new JSONObject();
                performanceJson.put("title", performance.getTitle());
                performanceJson.put("type", performance.getType().toString());
                performancesArray.add(performanceJson);
            }
            actorJson.put("performances", performancesArray);
            actorJson.put("biography", actor.getBiography());
            actorsArray.add(actorJson);
        }
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(actorsArray.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

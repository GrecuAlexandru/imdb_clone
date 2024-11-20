package org.example;

import org.example.gui.LoginGUI;
import org.example.terminal.Login;

import java.io.FileNotFoundException;
import java.util.*;

import org.example.utils.JsonParser;
import org.example.Notification;

/**
 * IMDB imdb = IMDB.getInstance();
 */
public class IMDB implements Subject{
    private static IMDB instance;

    private List<User> accounts; // might need to split into 3 lists
    private List<Actor> actors;
    private List<Request> requests;
    private List<Production> productions; // movies and series
    private List<Observer> observers;

    private IMDB() {
        observers = new ArrayList<>();
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }


    public void removeRequest(Request request) {
        requests.remove(request);
        JsonParser.writeRequestsJson();
    }

    public void addRequest(Request request) {
        requests.add(request);
        JsonParser.writeRequestsJson();
    }


    public void updateProduction(String title, Production newProduction) {
        Production oldProduction = findProduction(title);
        oldProduction.title = newProduction.title;
        oldProduction.directors = newProduction.directors;
        oldProduction.actors = newProduction.actors;
        oldProduction.genres = newProduction.genres;
        oldProduction.ratings = newProduction.ratings;
        oldProduction.plot = newProduction.plot;
        oldProduction.averageRating = newProduction.averageRating;
        oldProduction.releaseYear = newProduction.releaseYear;
        if (oldProduction.type == ProductionType.Movie) {
            ((Movie) oldProduction).duration = ((Movie) newProduction).duration;
        } else {
            ((Series) oldProduction).numSeasons = ((Series) newProduction).numSeasons;
            ((Series) oldProduction).seasons = ((Series) newProduction).seasons;
        }
        JsonParser.writeProductionsJson();
    }

    public void updateActor(String name, Actor newActor) {
        Actor oldActor = findActor(name);
        oldActor.name = newActor.name;
        oldActor.biography = newActor.biography;
        oldActor.performances = newActor.performances;
        JsonParser.writeActorsJson();
    }

    public List<Production> getProductions() {
        return productions;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<User> getAccounts() {
        return accounts;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public Production findProduction(String title) {
        for (Production production : productions) {
            if (production.title.equals(title)) {
                return production;
            }
        }
        return null;
    }

    public Actor findActor(String name) {
        for (Actor actor : actors) {
            if (actor.name.equals(name)) {
                return actor;
            }
        }
        return null;
    }

    public AccountType getTypeByUsername(String username) {
        for (User account : accounts) {
            if (account.getUsername().equals(username)) {
                return account.getType();
            }
        }
        return null;
    }

    public int findUserIndex(String username) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }



    public void parseAll() throws FileNotFoundException {
        productions = JsonParser.loadProductionsFromFile("src/main/resources/input/production.json");
        actors = JsonParser.loadActorsFromFile("src/main/resources/input/actors.json");
        accounts = JsonParser.loadAccountsFromFile("src/main/resources/input/accounts.json");
        requests = JsonParser.loadRequestsFromFile("src/main/resources/input/requests.json");

        for (Production production : productions) {
            if (production.getRatings() != null) {
                for (Rating rating : production.getRatings()) {
                    // we add observer to rating
                    for (Rating rating1 : production.getRatings()) {
                        // we get all other ratings and get the username of the people who rated
                        if (rating != rating1) {
                            String username = rating1.getUsername();
                            // we find the user with that username
                            int userIdx = IMDB.getInstance().findUserIndex(username);
                            if (userIdx != -1)
                                rating.addObserver(IMDB.getInstance().getAccounts().get(userIdx));
                        }
                    }
                }
            }
        }
    }

    public void run() throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose interface:");
        System.out.println("1. Terminal");
        System.out.println("2. Graphical Interface");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                Login.login();
                break;
            case 2:
                new LoginGUI();
                break;
            default:
                System.out.println("Invalid choice. Please enter 1 for Terminal or 2 for Graphical Interface.");
                run();
                break;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        IMDB imdb = IMDB.getInstance();
        imdb.run();
    }

    public List<Observer> getObservers() {
        return observers;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Notification notification) {
        for (Observer o : observers) {
            User user = (User) o;
            System.out.println(user.getUsername());
            if (user.getUsername().equals(notification.getToUsername())) {
                o.update(notification);
            }
        }
    }
}

package org.example.terminal;

import org.example.*;
import org.example.gui.RequestsPanelGUI;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

public class Requests {
    public static void requests(int userIndex) throws FileNotFoundException {
        System.out.println("Requests");
        System.out.println("Choose action:");
        System.out.println("    1) Add a request");
        System.out.println("    2) Delete a request");
        System.out.println("    3) Go back");

        Scanner scanner = new Scanner(System.in);

        String input = scanner.next();

        switch(input) {
            case "1":
                addRequest(userIndex, scanner);
                requests(userIndex);
                break;
            case "2":
                deleteRequest(userIndex, scanner);
                requests(userIndex);
                break;
            case "3":
                Menu.menu(userIndex);
                break;
            default:
                System.out.println("Invalid input");
                requests(userIndex);
        }
    }

    private static void addRequest(int userIndex, Scanner scanner) {
        System.out.println("Enter the request type:");
        System.out.println("    1) DELETE_ACCOUNT");
        System.out.println("    2) ACTOR_ISSUE");
        System.out.println("    3) MOVIE_ISSUE");
        System.out.println("    4) OTHERS");

        String input = scanner.next();

        RequestTypes requestType = null;

        switch(input) {
            case "1":
                requestType = RequestTypes.DELETE_ACCOUNT;
                break;
            case "2":
                requestType = RequestTypes.ACTOR_ISSUE;
                break;
            case "3":
                requestType = RequestTypes.MOVIE_ISSUE;
                break;
            case "4":
                requestType = RequestTypes.OTHERS;
                break;
            default:
                System.out.println("Invalid input");
                addRequest(userIndex, scanner);
        }

        String subject = null;
        if (requestType == RequestTypes.MOVIE_ISSUE) {
            System.out.println("Enter the movie title:");
            scanner.nextLine();
            subject = scanner.nextLine();
        } else if (requestType == RequestTypes.ACTOR_ISSUE) {
            System.out.println("Enter the actor name:");
            scanner.nextLine();
            subject = scanner.nextLine();
        }

        System.out.println("Enter the description:");
        String description = scanner.nextLine();

        String to = null;
        if (requestType == RequestTypes.DELETE_ACCOUNT || requestType == RequestTypes.OTHERS) {
            to = "ADMIN";
        } else if (requestType == RequestTypes.ACTOR_ISSUE) {
            for(int i = 0; i < IMDB.getInstance().getAccounts().size(); i++) {
                if (IMDB.getInstance().getAccounts().get(i).getType() == AccountType.Contributor) {
                    Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(i);
                    if (contributor.checkContribution(subject)) {
                        to = contributor.getUsername();
                    }
                } else if(IMDB.getInstance().getAccounts().get(i).getType() == AccountType.Admin) {
                    Admin admin = (Admin) IMDB.getInstance().getAccounts().get(i);
                    if (admin.checkContribution(subject)) {
                        to = admin.getUsername();
                    }
                }
            }

            if (to == null) {
                to = "ADMIN";
            }
        } else if (requestType == RequestTypes.MOVIE_ISSUE) {
            for(int i = 0; i < IMDB.getInstance().getAccounts().size(); i++) {
                if (IMDB.getInstance().getAccounts().get(i).getType() == AccountType.Contributor) {
                    Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(i);
                    if (contributor.checkContribution(subject)) {
                        to = contributor.getUsername();
                    }
                } else if(IMDB.getInstance().getAccounts().get(i).getType() == AccountType.Admin) {
                    Admin admin = (Admin) IMDB.getInstance().getAccounts().get(i);
                    if (admin.checkContribution(subject)) {
                        to = admin.getUsername();
                    }
                }
            }

            if (to == null) {
                to = "ADMIN";
            }
        }

        String username = IMDB.getInstance().getAccounts().get(userIndex).getUsername();
        LocalDateTime createdDate = LocalDateTime.now();
        String actorName = null;
        String movieTitle = null;
        if (requestType == RequestTypes.ACTOR_ISSUE) {
            actorName = subject;
        } else if (requestType == RequestTypes.MOVIE_ISSUE) {
            movieTitle = subject;
        }
        Request request = new Request(requestType, createdDate, username, to, description, actorName, movieTitle);

        IMDB.getInstance().addObserver(IMDB.getInstance().getAccounts().get(userIndex));
        if (!Objects.equals(to, "ADMIN")) {
            IMDB.getInstance().addObserver(IMDB.getInstance().getAccounts().get(IMDB.getInstance().findUserIndex(to)));
        }
        IMDB.getInstance().notifyObservers(new Notification(to, "You have a new request from " + username));
        IMDB.getInstance().addRequest(request);

        System.out.println("Request added");
    }

    private static void deleteRequest(int userIndex, Scanner scanner) {
        System.out.println("Choose the request you want to delete:");
        for (int i = 0; i < IMDB.getInstance().getRequests().size(); i++) {
            System.out.println("    " + (i + 1) + ") " + IMDB.getInstance().getRequests().get(i).getDescription());
        }

        String input = scanner.next();
        int requestToDeleteIndex = Integer.parseInt(input) - 1;
        IMDB.getInstance().removeRequest(IMDB.getInstance().getRequests().get(requestToDeleteIndex));

        System.out.println("Request deleted");
    }
}

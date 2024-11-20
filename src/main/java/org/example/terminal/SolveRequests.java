package org.example.terminal;

import org.example.*;
import org.example.expstrategy.calculateFromRequest;
import org.example.gui.SolveRequestsPanelGUI;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class SolveRequests {
    public static void solveRequests(int userIndex) {
        System.out.println("Solve requests");

        if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
            RequestsHolder.loadAdminRequests();
            List<Request> adminRequests = RequestsHolder.getAdminRequestsList();

            List<Request> myRequests = new ArrayList<>();
            for (Request request : adminRequests) {
                myRequests.add(request);
            }

            for (Request request : IMDB.getInstance().getRequests()) {
                if (request.getTo().equals(IMDB.getInstance().getAccounts().get(userIndex).getUsername())) {
                    myRequests.add(request);
                }
            }

            for (int i = 0; i < myRequests.size(); i++) {
                System.out.println((i+1) + ") ");
                myRequests.get(i).displayInfo();
            }

            System.out.println("Choose the request you want to solve:");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.next();
            int requestIndex = Integer.parseInt(input) - 1;

            solveRequest(myRequests.get(requestIndex), scanner, userIndex);
        }
    }

    private static void solveRequest(Request request, Scanner scanner, int userIndex) {
        System.out.println("Choose action:");
        System.out.println("    1) Accept");
        System.out.println("    2) Reject");
        System.out.println("    3) Go back");

        String input = scanner.next();

        switch(input) {
            case "1":
                if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
                    if (Objects.equals(request.getTo(), "ADMIN")) {
                        RequestsHolder.removeAdminRequest(request);
                    } else {
                        IMDB.getInstance().removeRequest(request);
                    }
                } else {
                    IMDB.getInstance().removeRequest(request);
                }
                String message = "Your request has been solved by " + IMDB.getInstance().getAccounts().get(userIndex).getUsername()
                        + " on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                boolean found = false;
                for(Observer o : IMDB.getInstance().getObservers()) {
                    User u = (User) o;
                    if (u.getUsername().equals(request.getUsername())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    int toIndex =  IMDB.getInstance().findUserIndex(request.getUsername());
                    IMDB.getInstance().addObserver(IMDB.getInstance().getAccounts().get(toIndex));
                }
                System.out.println("Notifying: " + request.getUsername());
                if (request.getRequestType() == RequestTypes.ACTOR_ISSUE || request.getRequestType() == RequestTypes.MOVIE_ISSUE) {
                    IMDB.getInstance().getAccounts().get(IMDB.getInstance().findUserIndex(request.getUsername())).updateExperience(new calculateFromRequest());
                }
                IMDB.getInstance().notifyObservers(new Notification(request.getUsername(), message));
                IMDB.getInstance().removeObserver(IMDB.getInstance().getAccounts().get(userIndex));
                break;
            case "2":
                if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
                    if (Objects.equals(request.getTo(), "ADMIN")) {
                        RequestsHolder.removeAdminRequest(request);
                    } else {
                        IMDB.getInstance().removeRequest(request);
                    }
                    // remove it from the list from SolveRequestsPanel
                    SolveRequestsPanelGUI.removeRequestFromList(request);
                } else {
                    // check if user is moderator
                    SolveRequestsPanelGUI.removeRequestFromList(request);
                    IMDB.getInstance().removeRequest(request);
                }
                String msg = "Your request has been rejected by " + IMDB.getInstance().getAccounts().get(userIndex).getUsername()
                        + " on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                boolean f = false;
                for(Observer o : IMDB.getInstance().getObservers()) {
                    User u = (User) o;
                    if (u.getUsername().equals(request.getUsername())) {
                        f = true;
                        break;
                    }
                }
                if (!f) {
                    int toIndex =  IMDB.getInstance().findUserIndex(request.getUsername());
                    IMDB.getInstance().addObserver(IMDB.getInstance().getAccounts().get(toIndex));
                }
                IMDB.getInstance().notifyObservers(new Notification(request.getUsername(), msg));
                IMDB.getInstance().removeObserver(IMDB.getInstance().getAccounts().get(userIndex));
                break;
            case "3":
                solveRequests(IMDB.getInstance().findUserIndex(request.getTo()));
                break;
            default:
                System.out.println("Invalid input");
                solveRequest(request, scanner, userIndex);
        }
    }
}

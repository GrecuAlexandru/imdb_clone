package org.example.gui;

import org.example.*;
import org.example.expstrategy.calculateFromRequest;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class RequestPagePanel extends JFrame {
    public static JPanel createRequestPagePanel(int userIndex, JPanel cardPanel, CardLayout cardLayout, Request request, int start) {
        if (request == null) {
            JPanel emptyPanel = new JPanel();
            return emptyPanel;
        }
        JPanel requestSolverPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Request Page", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 50)); // Increase the font size
        requestSolverPanel.add(titleLabel, BorderLayout.NORTH);

        // Request details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Username: " + request.getUsername());
        detailsPanel.add(usernameLabel);

        JLabel dateLabel = new JLabel("Date: " + request.getCreatedDate());
        detailsPanel.add(dateLabel);

        if (request.getRequestType() == RequestTypes.ACTOR_ISSUE) {
            JLabel actorLabel = new JLabel("Actor: " + request.getActorName());
            detailsPanel.add(actorLabel);
        } else if (request.getRequestType() == RequestTypes.MOVIE_ISSUE) {
            JLabel movieLabel = new JLabel("Movie: " + request.getMovieTitle());
            detailsPanel.add(movieLabel);
        }

        JLabel requestTypeLabel = new JLabel("Request type: " + request.getRequestType());
        detailsPanel.add(requestTypeLabel);

        // to
        JLabel toLabel = new JLabel("To: " + request.getTo());
        detailsPanel.add(toLabel);

        requestSolverPanel.add(detailsPanel, BorderLayout.WEST);

        // Request description
        JTextArea descriptionArea = new JTextArea(request.getDescription());
        descriptionArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        requestSolverPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout());

        if (request.getUsername().equals(IMDB.getInstance().getAccounts().get(userIndex).getUsername())) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(e -> {
                IMDB.getInstance().removeRequest(request);
                RequestsPanelGUI.removeRequestFromList(request);
                cardLayout.show(cardPanel, "requestsPanel");
            });
            southPanel.add(deleteButton);
        } else {
            // Solve button
            JButton solveButton = new JButton("Solve");
            solveButton.addActionListener(e -> {
                // check if user is admin
                if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
                    if (Objects.equals(request.getTo(), "ADMIN")) {
                        RequestsHolder.removeAdminRequest(request);
                    } else {
                        IMDB.getInstance().removeRequest(request);
                    }
                    // remove it from the list from SolveRequestsPanel
                    SolveRequestsPanelGUI.removeRequestFromList(request);
                } else {
                    // check if user is contributor
                    SolveRequestsPanelGUI.removeRequestFromList(request);
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
                cardLayout.show(cardPanel, "solveRequestsPanel");
                IMDB.getInstance().removeObserver(IMDB.getInstance().getAccounts().get(userIndex));
            });
            requestSolverPanel.add(solveButton, BorderLayout.EAST);

            // Reject button
            JButton rejectButton = new JButton("Reject");
            rejectButton.addActionListener(e -> {
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
                String message = "Your request has been rejected by " + IMDB.getInstance().getAccounts().get(userIndex).getUsername()
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
                IMDB.getInstance().notifyObservers(new Notification(request.getUsername(), message));
                cardLayout.show(cardPanel, "solveRequestsPanel");
                IMDB.getInstance().removeObserver(IMDB.getInstance().getAccounts().get(userIndex));
            });
            southPanel.add(rejectButton);
        }

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            if (start == 1) {
                cardLayout.show(cardPanel, "requestsPanel");
            } else {
                cardLayout.show(cardPanel, "solveRequestsPanel");
            }
        });
        southPanel.add(backButton);

        requestSolverPanel.add(southPanel, BorderLayout.SOUTH);

        return requestSolverPanel;
    }
}

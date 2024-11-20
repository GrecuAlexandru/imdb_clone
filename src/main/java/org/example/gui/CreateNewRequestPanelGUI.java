package org.example.gui;

import org.example.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.LocalDateTime;
import java.util.Objects;

public class CreateNewRequestPanelGUI {
    public static JPanel createNewRequestPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("New Request", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 36));
        panel.add(titleLabel);

        // Dropdown list for the type of request
        JLabel requestTypeLabel = new JLabel("Request type: ");
        panel.add(requestTypeLabel);
        String[] requestTypes = {"DELETE_ACCOUNT", "ACTOR_ISSUE", "MOVIE_ISSUE", "OTHERS"}; // Replace with your actual request types
        JComboBox<String> requestTypeComboBox = new JComboBox<>(requestTypes);
        panel.add(requestTypeComboBox);



        // if ACOTR_ISSUE or MOVIE_ISSUE was selected, ask for the title
        JLabel t = new JLabel("Title: ");
        panel.add(t);
        JTextField titleField = new JTextField();
        panel.add(titleField);
        titleField.setEnabled(false);

        requestTypeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedType = (String) e.getItem();
                if ("ACTOR_ISSUE".equals(selectedType) || "MOVIE_ISSUE".equals(selectedType)) {
                    titleField.setEnabled(true);
                } else {
                    titleField.setEnabled(false);
                }
            }
        });


        // Input field for the description
        JLabel descriptionLabel = new JLabel("Description: ");
        panel.add(descriptionLabel);
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setRows(5);
        descriptionArea.setColumns(20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        panel.add(scrollPane);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String requestType = (String) requestTypeComboBox.getSelectedItem();
            RequestTypes type = RequestTypes.valueOf(requestType);
            String title = titleField.getText();
            String description = descriptionArea.getText();

            String to = null;
            if (type == RequestTypes.DELETE_ACCOUNT || type == RequestTypes.OTHERS) {
                to = "ADMIN";
            } else if (type == RequestTypes.ACTOR_ISSUE) {
                for(int i = 0; i < IMDB.getInstance().getAccounts().size(); i++) {
                    if (IMDB.getInstance().getAccounts().get(i).getType() == AccountType.Contributor) {
                        Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(i);
                        if (contributor.checkContribution(title)) {
                            to = contributor.getUsername();
                        }
                    } else if(IMDB.getInstance().getAccounts().get(i).getType() == AccountType.Admin) {
                        Admin admin = (Admin) IMDB.getInstance().getAccounts().get(i);
                        if (admin.checkContribution(title)) {
                            to = admin.getUsername();
                        }
                    }
                }

                if (to == null) {
                    to = "ADMIN";
                }
            } else if (type == RequestTypes.MOVIE_ISSUE) {
                for(int i = 0; i < IMDB.getInstance().getAccounts().size(); i++) {
                    if (IMDB.getInstance().getAccounts().get(i).getType() == AccountType.Contributor) {
                        Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(i);
                        if (contributor.checkContribution(title)) {
                            to = contributor.getUsername();
                        }
                    } else if(IMDB.getInstance().getAccounts().get(i).getType() == AccountType.Admin) {
                        Admin admin = (Admin) IMDB.getInstance().getAccounts().get(i);
                        if (admin.checkContribution(title)) {
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
            if (type == RequestTypes.ACTOR_ISSUE) {
                actorName = title;
            } else if (type == RequestTypes.MOVIE_ISSUE) {
                movieTitle = title;
            }
            Request request = new Request(type, createdDate, username, to, description, actorName, movieTitle);

            IMDB.getInstance().addObserver(IMDB.getInstance().getAccounts().get(userIndex));
            if (!Objects.equals(to, "ADMIN")) {
                IMDB.getInstance().addObserver(IMDB.getInstance().getAccounts().get(IMDB.getInstance().findUserIndex(to)));
            }
            IMDB.getInstance().notifyObservers(new Notification(to, "You have a new request from " + username));
            IMDB.getInstance().addRequest(request);

            // Add the request to the list
            RequestsPanelGUI.addRequestToList(request);

            cardLayout.show(cardPanel, "requestsPanel");
        });

        panel.add(submitButton);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "requestsPanel");
        });
        panel.add(backButton);

        return panel;
    }
}
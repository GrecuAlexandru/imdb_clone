package org.example.gui;

import org.example.*;
import org.example.expstrategy.calculateFromRating;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductionPanelGUI extends JFrame{
    public static JPanel createProductionPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout, Production production) {
        JPanel productionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title
        JLabel titleLabel = new JLabel(production.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 36));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        productionPanel.add(titleLabel, gbc);

        // Production details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        JLabel yearLabel = new JLabel("Year: " + production.getReleaseYear());
        detailsPanel.add(yearLabel);

        JLabel typeLabel = new JLabel("Type: " + production.getType());
        detailsPanel.add(typeLabel);

        if (production.getType().equals("Movie")) {
            JLabel durationLabel = new JLabel("Duration: " + production.getDuration());
            detailsPanel.add(durationLabel);
        } else {
            JLabel numberOfSeasonsLabel = new JLabel("Number of seasons: " + production.getSeasons().size());
            detailsPanel.add(numberOfSeasonsLabel);
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        productionPanel.add(detailsPanel, gbc);

        // Production description
        JTextArea descriptionArea = new JTextArea(production.getPlot());
        descriptionArea.setLineWrap(true); // Enable line wrapping
        descriptionArea.setWrapStyleWord(true); // Wrap lines at word boundaries
        descriptionArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(200, 100)); // Set preferred size

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        productionPanel.add(scrollPane, gbc);

        // Add/Remove favorties button
        JButton favoritesButton;
        if (IMDB.getInstance().getAccounts().get(userIndex).getFavorites().contains(production)) {
            favoritesButton = new JButton("Remove from favorites");
        } else {
            favoritesButton = new JButton("Add to favorites");
        }
        favoritesButton.addActionListener(e -> {
            if (favoritesButton.getText().equals("Remove from favorites")) {
                IMDB.getInstance().getAccounts().get(userIndex).removeFavorite((Comparable<?>) production);
                favoritesButton.setText("Add to favorites");
                FavoritesPanelGUI.updateFavoritesPanel();
            }
            else if (favoritesButton.getText().equals("Add to favorites")) {
                IMDB.getInstance().getAccounts().get(userIndex).addFavorite((Comparable<?>) production);
                favoritesButton.setText("Remove from favorites");
                FavoritesPanelGUI.updateFavoritesPanel();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        productionPanel.add(favoritesButton, gbc);

        if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Regular) {
            // check if i already rated this production
            String username = IMDB.getInstance().getAccounts().get(userIndex).getUsername();
            boolean rated = false;
            if (production.getRatings() != null) {
                for (Rating rating : production.getRatings()) {
                    if (rating.getUsername().equals(username)) {
                        rated = true;
                        break;
                    }
                }
            }

            if (rated) {
                JButton deleteRatingButton = new JButton("Delete Rating");
                deleteRatingButton.addActionListener(e -> {
                    Rating userRating = null;
                    for (Rating rating : production.getRatings()) {
                        if (rating.getUsername().equals(username)) {
                            userRating = rating;
                            break;
                        }
                    }
                    if (userRating != null) {
                        userRating.removeObserver(IMDB.getInstance().getAccounts().get(userIndex));
                        production.deleteRating(userRating);
                        JOptionPane.showMessageDialog(null, "Your rating has been deleted.");
                    } else {
                        JOptionPane.showMessageDialog(null, "You have not rated this production yet.");
                    }
                });

                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.gridwidth = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                productionPanel.add(deleteRatingButton, gbc);
            } else {
                JButton addRatingButton = new JButton("Add Rating");
                addRatingButton.addActionListener(e -> {
                    String ratingStr = JOptionPane.showInputDialog("Enter your rating (0-10):");
                    String comment = JOptionPane.showInputDialog("Enter your comment:");
                    try {
                        int rating = Integer.parseInt(ratingStr);
                        if (rating >= 0 && rating <= 10 && comment != null && !comment.isEmpty()) {
                            Rating newRating = new Rating(username, rating, comment);
                            // add observers
                            for (Rating rating1 : production.getRatings()) {
                                // we get all other ratings and get the username of the people who rated
                                String username1 = rating1.getUsername();
                                // we find the user with that username
                                int userIdx = IMDB.getInstance().findUserIndex(username1);
                                newRating.addObserver(IMDB.getInstance().getAccounts().get(userIdx));
                            }
                            String message = username + " added a rating to " + production.getTitle() + "(You also rated this production)";
                            newRating.notifyObservers(new Notification(username, message));
                            newRating.addObserver(IMDB.getInstance().getAccounts().get(userIndex));
                            production.addRating(newRating);
                            IMDB.getInstance().getAccounts().get(userIndex).updateExperience(new calculateFromRating());
                            JOptionPane.showMessageDialog(null, "Your rating has been added.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
                    }
                });

                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.gridwidth = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                productionPanel.add(addRatingButton, gbc);
            }
        }

        // Ratings
        JPanel ratingsPanel = new JPanel();
        ratingsPanel.setLayout(new BoxLayout(ratingsPanel, BoxLayout.Y_AXIS));
        if (production.getRatings() != null) {
            if (production.getRatings().size() == 0) {
                JLabel noRatingsLabel = new JLabel("No ratings yet.");
                ratingsPanel.add(noRatingsLabel);
            }
            else {
                JLabel ratingsLabel = new JLabel("Ratings:");
                ratingsPanel.add(ratingsLabel);

                // Create a new list of ratings and sort it based on user's experience
                List<Rating> sortedRatings = new ArrayList<>(production.getRatings());
                sortedRatings.sort(Comparator.comparingInt((Rating r) -> IMDB.getInstance().getAccounts().get(IMDB.getInstance().findUserIndex(r.getUsername())).getExperience()).reversed());

                for (Rating rating : sortedRatings) {
                    JLabel ratingLabel = new JLabel("User: " + rating.getUsername() + "(" + IMDB.getInstance().getAccounts().get(IMDB.getInstance().findUserIndex(rating.getUsername())).getExperience() + ")" + " Rating: " + rating.getRating() + ", Comment: " + rating.getComment());
                    ratingsPanel.add(ratingLabel);
                }
            }
        }

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        productionPanel.add(ratingsPanel, gbc);

        // Directors
        JPanel directorsPanel = new JPanel();
        directorsPanel.setLayout(new BoxLayout(directorsPanel, BoxLayout.Y_AXIS));
        JLabel directorsLabel = new JLabel("Directors:");
        directorsPanel.add(directorsLabel);
        for (String director : production.getDirectors()) {
            JLabel directorLabel = new JLabel(director);
            directorsPanel.add(directorLabel);
        }

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        productionPanel.add(directorsPanel, gbc);

        // Actors
        JPanel actorsPanel = new JPanel();
        actorsPanel.setLayout(new BoxLayout(actorsPanel, BoxLayout.Y_AXIS));
        JLabel actorsLabel = new JLabel("Actors:");
        actorsPanel.add(actorsLabel);
        for (String actor : production.getActors()) {
            JLabel actorLabel = new JLabel(actor);
            actorsPanel.add(actorLabel);
        }

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        productionPanel.add(actorsPanel, gbc);

        // Go back button
        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "viewProductionsPanel");
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        productionPanel.add(goBackButton, gbc);

        return productionPanel;
    }
}
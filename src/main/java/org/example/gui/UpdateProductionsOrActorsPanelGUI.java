package org.example.gui;

import org.example.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class UpdateProductionsOrActorsPanelGUI {
    public static JPanel createUpdateProductionsOrActorsPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        JPanel updateProductionsOrActorsPanel = new JPanel();
        updateProductionsOrActorsPanel.setLayout(new GridLayout(2, 1));

        // Create a list model for productions
        DefaultListModel<String> productionsListModel = new DefaultListModel<>();
        for (Production production : IMDB.getInstance().getProductions()) {
            productionsListModel.addElement(production.getTitle());
        }

        // Create a list for productions and add it to a scroll pane
        JList<String> productionsList = new JList<>(productionsListModel);
        JScrollPane productionsScrollPane = new JScrollPane(productionsList);
        updateProductionsOrActorsPanel.add(productionsScrollPane);

        // Create a list model for actors
        DefaultListModel<String> actorsListModel = new DefaultListModel<>();
        for (Actor actor : IMDB.getInstance().getActors()) {
            actorsListModel.addElement(actor.getName());
        }

        // Create a list for actors and add it to a scroll pane
        JList<String> actorsList = new JList<>(actorsListModel);
        JScrollPane actorsScrollPane = new JScrollPane(actorsList);
        updateProductionsOrActorsPanel.add(actorsScrollPane);

        productionsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click detected
                    int index = productionsList.locationToIndex(e.getPoint());
                    String oldTitle = productionsListModel.getElementAt(index);
                    String newTitle = JOptionPane.showInputDialog("Enter the new title for the production:", oldTitle);
                    String oldPlot = IMDB.getInstance().findProduction(oldTitle).getPlot();
                    String newPlot = JOptionPane.showInputDialog("Enter the new plot for the production:", oldPlot);
                    String oldType = IMDB.getInstance().findProduction(oldTitle).getType().toString();
                    String oldReleaseYear = String.valueOf(IMDB.getInstance().findProduction(oldTitle).getReleaseYear());
                    String newReleaseYear = JOptionPane.showInputDialog("Enter the new release year for the production:", oldReleaseYear);
                    String oldAverageRating = String.valueOf(IMDB.getInstance().findProduction(oldTitle).getAverageRating());
                    String newAverageRating = JOptionPane.showInputDialog("Enter the new average rating for the production:", oldAverageRating);

                    // old directors
                    String oldDirectors = "";
                    for (String director : IMDB.getInstance().findProduction(oldTitle).getDirectors()) {
                        oldDirectors += director + ", ";
                    }
                    oldDirectors = oldDirectors.substring(0, oldDirectors.length() - 2);
                    String newDirectors = JOptionPane.showInputDialog("Enter the new directors for the production (separated by commas):", oldDirectors);
                    List<String> newDirectorsArray = List.of(newDirectors.split(", "));

                    // old actors
                    String oldActors = "";
                    for (String actor : IMDB.getInstance().findProduction(oldTitle).getActors()) {
                        oldActors += actor + ", ";
                    }
                    oldActors = oldActors.substring(0, oldActors.length() - 2);
                    String newActors = JOptionPane.showInputDialog("Enter the new actors for the production (separated by commas):", oldActors);
                    List<String> newActorsArray = List.of(newActors.split(", "));

                    // old genres
                    String oldGenres = "";
                    for (String genre : IMDB.getInstance().findProduction(oldTitle).getGenres()) {
                        oldGenres += genre + ", ";
                    }
                    oldGenres = oldGenres.substring(0, oldGenres.length() - 2);
                    String newGenres = JOptionPane.showInputDialog("Enter the new genres for the production (separated by commas):", oldGenres);
                    List<String> newGenresArray = List.of(newGenres.split(", "));

                    if (oldType == ProductionType.Movie.toString()) {
                        String oldDuration = IMDB.getInstance().findProduction(oldTitle).getDuration();
                        String newDuration = JOptionPane.showInputDialog("Enter the new duration for the production (in minutes):", oldDuration);
                        String newDurationString = newDuration + " minutes";
                        Movie newMovie = new Movie(
                                newTitle,
                                ProductionType.Movie,
                                newDirectorsArray,
                                newActorsArray,
                                newGenresArray,
                                IMDB.getInstance().findProduction(oldTitle).getRatings(),
                                newPlot,
                                Double.parseDouble(newAverageRating),
                                Integer.parseInt(newReleaseYear),
                                newDurationString
                        );
                        // Update the production in the database
                        IMDB.getInstance().updateProduction(oldTitle, newMovie);
                        // Update the list model
                        productionsListModel.setElementAt(newTitle, index);
                    } else {
                        String oldNumberOfSeasons = String.valueOf(IMDB.getInstance().findProduction(oldTitle).getSeasons().size());
                        String newNumberOfSeasons = JOptionPane.showInputDialog("Enter the new number of seasons for the production:", oldNumberOfSeasons);
                        Map<String, List<Episode>> newSeasons = new HashMap<>();
                        for (int i = 0; i < Integer.parseInt(newNumberOfSeasons); i++) {
                            String seasonName = "Season " + (i + 1);
                            String oldNumberOfEpisodes = String.valueOf(IMDB.getInstance().findProduction(oldTitle).getSeasons().get(seasonName).size());
                            String newNumberOfEpisodes = JOptionPane.showInputDialog("Enter the new number of episodes for season " + (i + 1) + ":", oldNumberOfEpisodes);
                            List<Episode> episodes = new ArrayList<>();
                            for (int j = 0; j < Integer.parseInt(newNumberOfEpisodes); j++) {
                                String episodeName = JOptionPane.showInputDialog("Enter the name of episode " + (j + 1) + " of season " + (i + 1) + ":");
                                String oldEpisodeDuration = IMDB.getInstance().findProduction(oldTitle).getSeasons().get(seasonName).get(j).getDuration();
                                String newEpisodeDuration = JOptionPane.showInputDialog("Enter the new duration of episode " + (j + 1) + " of season " + (i + 1) + ":", oldEpisodeDuration);
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
                                IMDB.getInstance().findProduction(oldTitle).getRatings(),
                                newPlot,
                                Double.parseDouble(newAverageRating),
                                Integer.parseInt(newReleaseYear),
                                Integer.parseInt(newNumberOfSeasons),
                                newSeasons
                        );
                        // Update the production in the database
                        IMDB.getInstance().updateProduction(oldTitle, newSeries);
                        // Update the list model
                        productionsListModel.setElementAt(newTitle, index);
                    }
                }
            }
        });

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "menuPanel");
        });
        updateProductionsOrActorsPanel.add(backButton);

        // Add a MouseListener to the actors list
        actorsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click detected
                    int index = actorsList.locationToIndex(e.getPoint());
                    String oldName = actorsListModel.getElementAt(index);
                    String newName = JOptionPane.showInputDialog("Enter the new name for the actor:", oldName);
                    String oldBiography = IMDB.getInstance().findActor(oldName).getBiography();
                    String newBiography = JOptionPane.showInputDialog("Enter the new biography for the actor:", oldBiography);

                    // performances
                    String oldPerformances = "";
                    for (Actor.Performance performance : IMDB.getInstance().findActor(oldName).getPerformances()) {
                        oldPerformances += performance.getTitle() + " (" + performance.getType() + "), ";
                    }
                    oldPerformances = oldPerformances.substring(0, oldPerformances.length() - 2);
                    String newPerformances = JOptionPane.showInputDialog("Enter the new performances for the actor (separated by commas):", oldPerformances);
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
                    IMDB.getInstance().updateActor(oldName, newActor);
                    // Update the list model
                    actorsListModel.setElementAt(newName, index);
                }
            }
        });

        return updateProductionsOrActorsPanel;
    }
}

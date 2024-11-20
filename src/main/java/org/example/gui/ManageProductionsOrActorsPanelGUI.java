package org.example.gui;

import org.example.*;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class ManageProductionsOrActorsPanelGUI {
    public static JPanel createManageProductionsOrActorsPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        JPanel manageProductionsOrActorsPanel = new JPanel();
        manageProductionsOrActorsPanel.setLayout(new FlowLayout()); // Change GridLayout to FlowLayout

        JButton manageProductionsButton = new JButton("Add new Production");
        manageProductionsButton.setPreferredSize(new Dimension(300, 50)); // Set the preferred size of the button
        manageProductionsButton.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Enter the title of the production:");
            Integer releaseYear = Integer.parseInt(JOptionPane.showInputDialog("Enter the release year of the production:"));
            List<String> genres = new ArrayList<>();
            String genre = JOptionPane.showInputDialog("Enter a genre of the production (or press Cancel to finish):");
            while (genre != null) {
                genres.add(genre);
                genre = JOptionPane.showInputDialog("Enter a genre of the production (or press Cancel to finish):");
            }
            String plot = JOptionPane.showInputDialog("Enter the plot of the production:");
            List<String> directors = new ArrayList<>();
            String director = JOptionPane.showInputDialog("Enter a director of the production (or press Cancel to finish):");
            while (director != null) {
                directors.add(director);
                director = JOptionPane.showInputDialog("Enter a director of the production (or press Cancel to finish):");
            }
            List<String> actors = new ArrayList<>();
            String actor = JOptionPane.showInputDialog("Enter an actor of the production (or press Cancel to finish):");
            while (actor != null) {
                actors.add(actor);
                actor = JOptionPane.showInputDialog("Enter an actor of the production (or press Cancel to finish):");
            }
            double averageRating = Double.parseDouble(JOptionPane.showInputDialog("Enter the average rating of the production:"));
            String type = JOptionPane.showInputDialog("Enter the type of the production (movie or series):");
            ProductionType productionType = ProductionType.fromString(type);
            if (productionType == ProductionType.Movie) {
                int duration = Integer.parseInt(JOptionPane.showInputDialog("Enter the duration of the production (in minutes):"));
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
                Integer numberOfSeasons = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of seasons of the production:"));
                Map<String, List<Episode>> seasons = new HashMap<>();
                for (int i = 0; i < numberOfSeasons; i++) {
                    String seasonName = "Season " + (i + 1);
                    Integer numberOfEpisodes = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of episodes of season " + (i + 1) + ":"));
                    List<Episode> episodes = new ArrayList<>();
                    for (int j = 0; j < numberOfEpisodes; j++) {
                        String episodeName = JOptionPane.showInputDialog("Enter the name of episode " + (j + 1) + " of season " + (i + 1) + ":");
                        Integer episodeDuration = Integer.parseInt(JOptionPane.showInputDialog("Enter the duration of episode " + (j + 1) + " of season " + (i + 1) + ":"));
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
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
            }
        });

        JButton manageActorsButton = new JButton("Add new Actor");
        manageActorsButton.setPreferredSize(new Dimension(300, 50)); // Set the preferred size of the button
        manageActorsButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter the name of the actor:");
            String biography = JOptionPane.showInputDialog("Enter the biography of the actor:");
            List<Actor.Performance> performances = new ArrayList<>();
            int numPerformances = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of performances of the actor:"));
            for (int i = 0; i < numPerformances; i++) {
                String title = JOptionPane.showInputDialog("Enter the title of a performance of the actor (or press Cancel to finish):");
                String type = JOptionPane.showInputDialog("Enter the type of the performance (movie or series):");
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
        });

        manageProductionsOrActorsPanel.add(manageProductionsButton);
        manageProductionsOrActorsPanel.add(manageActorsButton);

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "menuPanel");
        });
        manageProductionsOrActorsPanel.add(goBackButton);

        // my added productions/actors
        // Create a list model for productions
        DefaultListModel<String> contributionList = new DefaultListModel<>();
        if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
            Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
            for (Object production : admin.getContributions()) {
                if (production instanceof Production) {
                    contributionList.addElement(((Production) production).getTitle());
                } else if (production instanceof Actor) {
                    contributionList.addElement(((Actor) production).getName());
                }
            }
        } else if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Contributor) {
            Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(userIndex);
            for (Object production : contributor.getContributions()) {
                if (production instanceof Production) {
                    contributionList.addElement(((Production) production).getTitle());
                } else if (production instanceof Actor) {
                    contributionList.addElement(((Actor) production).getName());
                }
            }
        }
        JList<String> contributionJList = new JList<>(contributionList);
        contributionJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contributionJList.setLayoutOrientation(JList.VERTICAL);
        contributionJList.setVisibleRowCount(-1);
        JScrollPane contributionScrollPane = new JScrollPane(contributionJList);
        contributionScrollPane.setPreferredSize(new Dimension(300, 300));
        manageProductionsOrActorsPanel.add(contributionScrollPane);

        // add delete button that deletes the selected contribution
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            String selectedContribution = contributionJList.getSelectedValue();
            if (selectedContribution != null) {
                if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
                    Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
                    Object contribution = admin.getContribution(selectedContribution);
                    if (contribution instanceof Production) {
                        Production production = (Production) contribution;
                        admin.removeProductionSystem(production.getTitle());
                    } else if (contribution instanceof Actor) {
                        admin.removeActorSystem(selectedContribution);
                    }
                } else {
                    Contributor contributor = (Contributor) IMDB.getInstance().getAccounts().get(userIndex);
                    Object contribution = contributor.getContribution(selectedContribution);
                    if (contribution instanceof Production) {
                        Production production = (Production) contribution;
                        contributor.removeProductionSystem(production.getTitle());
                    } else if (contribution instanceof Actor) {
                        contributor.removeActorSystem(selectedContribution);
                    }
                }
                contributionList.removeElement(selectedContribution);
            }
        });

        manageProductionsOrActorsPanel.add(deleteButton);

        return manageProductionsOrActorsPanel;
    }
}

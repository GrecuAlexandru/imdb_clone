package org.example.gui;

import org.example.Actor;
import org.example.IMDB;
import org.example.Production;

import javax.swing.*;
import java.awt.*;

public class ActorPanelGUI extends JFrame {
    public static JPanel createActorPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout, Actor actor) {
        JPanel actorPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel(actor.getName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 36));
        actorPanel.add(titleLabel, BorderLayout.NORTH);

        // Actor biography
        JTextArea biographyArea = new JTextArea(actor.getBiography());
        biographyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(biographyArea);
        actorPanel.add(scrollPane, BorderLayout.CENTER);

        // Actor performances
        JPanel performancesPanel = new JPanel();
        performancesPanel.setLayout(new BoxLayout(performancesPanel, BoxLayout.Y_AXIS));

        JLabel performancesLabel = new JLabel("Performances:");
        performancesPanel.add(performancesLabel);

        for (Actor.Performance performance : actor.getPerformances()) {
            JLabel performanceLabel = new JLabel(performance.getTitle() + " (" + performance.getType() + ")");
            performancesPanel.add(performanceLabel);
        }

        actorPanel.add(performancesPanel, BorderLayout.WEST);

        // Add/Remove favorties button
        JButton favoritesButton = new JButton("Add to favorites");
        favoritesButton.addActionListener(e -> {
            if (favoritesButton.getText().equals("Remove from favorites")) {
                IMDB.getInstance().getAccounts().get(userIndex).removeFavorite((Comparable<?>) actor);
                for (Object p : IMDB.getInstance().getAccounts().get(userIndex).getFavorites()) {
                    // p can be a Production or an Actor
                    if (p instanceof Production) {
                        Production x = (Production) p;
                        System.out.println(x.getTitle());
                    } else {
                        Actor x = (Actor) p;
                        System.out.println(x.getName());
                    }
                }
                favoritesButton.setText("Add to favorites");
            } else {
                IMDB.getInstance().getAccounts().get(userIndex).addFavorite((Comparable<?>) actor);
                for (Object p : IMDB.getInstance().getAccounts().get(userIndex).getFavorites()) {
                    // p can be a Production or an Actor
                    if (p instanceof Production) {
                        Production x = (Production) p;
                        System.out.println(x.getTitle());
                    } else {
                        Actor x = (Actor) p;
                        System.out.println(x.getName());
                    }
                }
                favoritesButton.setText("Remove from favorites");
            }
        });

        actorPanel.add(favoritesButton, BorderLayout.SOUTH);

        // Go back button
        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "mainPagePanel");
        });
        actorPanel.add(goBackButton, BorderLayout.EAST);

        return actorPanel;
    }
}

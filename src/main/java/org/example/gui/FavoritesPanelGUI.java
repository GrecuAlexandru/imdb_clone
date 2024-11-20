package org.example.gui;

import org.example.Production;

import javax.swing.*;
import java.awt.*;
import java.util.SortedSet;

public class FavoritesPanelGUI {
    private static JPanel favoritesListPanel;
    private static int userIndex;
    private static JPanel cardPanel;
    private static CardLayout cardLayout;
    public static JPanel createFavoritesPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        FavoritesPanelGUI.userIndex = userIndex;
        FavoritesPanelGUI.cardPanel = cardPanel;
        FavoritesPanelGUI.cardLayout = cardLayout;
        JPanel favoritesPanel = new JPanel();
        favoritesPanel.setLayout(new BorderLayout());

        JLabel favoritesLabel = new JLabel("Favorites");
        favoritesPanel.add(favoritesLabel, BorderLayout.NORTH);

        favoritesListPanel = new JPanel();
        favoritesListPanel.setLayout(new BoxLayout(favoritesListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(favoritesListPanel);
        favoritesPanel.add(scrollPane, BorderLayout.CENTER);

        // Fetch the favorites from the user and display them
        SortedSet<Comparable<?>> favorites = org.example.IMDB.getInstance().getAccounts().get(userIndex).getFavorites();
        for(Comparable<?> favorite : favorites) {
            JPanel favoritePanel = new JPanel(new BorderLayout());
            JLabel favoriteLabel;
            JButton removeButton = new JButton("Remove");

            if (favorite instanceof Production) {
                Production production = (Production) favorite;
                favoriteLabel = new JLabel("Production: " + production.getTitle());
                removeButton.addActionListener(e -> {
                    org.example.IMDB.getInstance().getAccounts().get(userIndex).removeFavorite((Comparable<?>) production);
                    favoritesListPanel.remove(favoritePanel);
                    favoritesListPanel.revalidate();
                    favoritesListPanel.repaint();
                });
            } else {
                org.example.Actor actor = (org.example.Actor) favorite;
                favoriteLabel = new JLabel("Actor: " + actor.getName());
                removeButton.addActionListener(e -> {
                    org.example.IMDB.getInstance().getAccounts().get(userIndex).removeFavorite(actor);
                    favoritesListPanel.remove(favoritePanel);
                    favoritesListPanel.revalidate();
                    favoritesListPanel.repaint();
                });
            }

            favoritePanel.add(favoriteLabel, BorderLayout.WEST);
            favoritePanel.add(removeButton, BorderLayout.EAST);
            favoritesListPanel.add(favoritePanel);
        }

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "mainPagePanel");
        });
        favoritesPanel.add(goBackButton, BorderLayout.SOUTH);

        return favoritesPanel;
    }

    public static void updateFavoritesPanel() {
        // Clear the current favorites list
        favoritesListPanel.removeAll();

        // Fetch the updated favorites from the user and display them
        SortedSet<Comparable<?>> favorites = org.example.IMDB.getInstance().getAccounts().get(userIndex).getFavorites();
        for(Comparable<?> favorite : favorites) {
            JPanel favoritePanel = new JPanel(new BorderLayout());
            JLabel favoriteLabel;
            JButton removeButton = new JButton("Remove");

            if (favorite instanceof Production) {
                Production production = (Production) favorite;
                favoriteLabel = new JLabel("Production: " + production.getTitle());
                removeButton.addActionListener(e -> {
                    org.example.IMDB.getInstance().getAccounts().get(userIndex).removeFavorite((Comparable<?>) production);
                    favoritesListPanel.remove(favoritePanel);
                    favoritesListPanel.revalidate();
                    favoritesListPanel.repaint();
                });
            } else {
                org.example.Actor actor = (org.example.Actor) favorite;
                favoriteLabel = new JLabel("Actor: " + actor.getName());
                removeButton.addActionListener(e -> {
                    org.example.IMDB.getInstance().getAccounts().get(userIndex).removeFavorite(actor);
                    favoritesListPanel.remove(favoritePanel);
                    favoritesListPanel.revalidate();
                    favoritesListPanel.repaint();
                });
            }

            favoritePanel.add(favoriteLabel, BorderLayout.WEST);
            favoritePanel.add(removeButton, BorderLayout.EAST);
            favoritesListPanel.add(favoritePanel);
        }

        // Revalidate and repaint the panel to reflect the changes
        favoritesListPanel.revalidate();
        favoritesListPanel.repaint();
    }
}

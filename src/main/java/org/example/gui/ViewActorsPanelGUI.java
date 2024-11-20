package org.example.gui;

import javax.swing.*;

import org.example.IMDB;
import org.example.Actor;
import org.example.Production;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewActorsPanelGUI extends JFrame {
    private static List<Actor> actors;

    public static JPanel createViewActorsPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        JPanel viewActorsPanel = new JPanel(new BorderLayout());

        // Retrieve the actors list
        actors = IMDB.getInstance().getActors();

        // Sort the actors list alphabetically
        Collections.sort(actors, Comparator.comparing(Actor::getName));

        // Create the JList
        JList<Actor> list = new JList<>(actors.toArray(new Actor[0]));

        list.setCellRenderer((list1, actor, index, isSelected, cellHasFocus) -> {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add space between each actor

            JLabel nameLabel = new JLabel("<html>" + (index + 1) + ". " + actor.getName() + "</html>"); // Add number in front of the name
            nameLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Make the name bigger and bolder
            panel.add(nameLabel, BorderLayout.NORTH);

            JTextArea biographyArea = new JTextArea(actor.getBiography());
            biographyArea.setLineWrap(true);
            biographyArea.setWrapStyleWord(true);
            biographyArea.setEditable(false);
            JScrollPane biographyScrollPane = new JScrollPane(biographyArea);
            biographyScrollPane.setPreferredSize(new Dimension(200, 50)); // Set the preferred size
            panel.add(biographyScrollPane, BorderLayout.CENTER);

            return panel;
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    Actor actor = list.getSelectedValue();
                    JPanel actorPanel = ActorPanelGUI.createActorPanel(userIndex, cardPanel, cardLayout, actor);
                    cardPanel.add(actorPanel, "actorPanel");
                    cardLayout.show(cardPanel, "actorPanel");
                }
            }
        });

        // Add the JList to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(list);
        list.setVisibleRowCount(10);

        // Set smooth scrolling
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16); // Adjust the increment value as needed for smoother scrolling

        // Add the JScrollPane to the panel
        viewActorsPanel.add(scrollPane, BorderLayout.CENTER);

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "menuPanel");
        });
        viewActorsPanel.add(goBackButton, BorderLayout.SOUTH);

        return viewActorsPanel;
    }

    // Method to show more details about the selected actor
    private static void showMoreDetails(Actor actor) {
        StringBuilder performances = new StringBuilder();
        for (Actor.Performance performance : actor.getPerformances()) {
            performances.append("Title: ").append(performance.getTitle())
                    .append(", Type: ").append(performance.getType())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(null, "Detailed Information:\n" +
                "Name: " + actor.getName() + "\n" +
                "Performances: \n" + performances.toString());
    }
}
package org.example.gui;

import org.example.Actor;
import org.example.IMDB;
import org.example.Production;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SearchPanelGUI {
    public static JPanel createSearchPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        JTextField searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.NORTH);

        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        searchPanel.add(scrollPane, BorderLayout.CENTER);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch(searchField, resultsArea));
        searchPanel.add(searchButton, BorderLayout.SOUTH);

        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch(searchField, resultsArea);
                }
            }
        });

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "mainPagePanel");
        });
        searchPanel.add(goBackButton, BorderLayout.EAST);

        return searchPanel;
    }

    private static void performSearch(JTextField searchField, JTextArea resultsArea) {
        String query = searchField.getText().toLowerCase();
        StringBuilder results = new StringBuilder();

        for (Production production : IMDB.getInstance().getProductions()) {
            if (production.getTitle().toLowerCase().contains(query)) {
                results.append("Production: ").append(production.getTitle()).append("\n");
            }
        }

        for (Actor actor : IMDB.getInstance().getActors()) {
            if (actor.getName().toLowerCase().contains(query)) {
                results.append("Actor: ").append(actor.getName()).append("\n");
            }
        }

        resultsArea.setText(results.toString());
    }
}

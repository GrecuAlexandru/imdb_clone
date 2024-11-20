package org.example.gui;

import org.example.IMDB;
import org.example.Production;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainPagePanelGUI extends JFrame {
    private static int userIndex;
    private static JPanel cardPanel;
    private static CardLayout cardLayout;

    public MainPagePanelGUI(int userIndex) {
        this.userIndex = userIndex;
        setTitle("IMDB");
        setSize(1280, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel mainPagePanel = createMainPagePanel();
        cardPanel.add(mainPagePanel, "mainPagePanel");
        cardLayout.show(cardPanel, "mainPagePanel");

        add(cardPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createMainPagePanel() {
        JPanel panel = new JPanel(new FlowLayout());

        // IMDB title
        JLabel titleLabel = new JLabel("IMDB", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 36));
        panel.add(titleLabel, BorderLayout.NORTH);

        // hello message
        JLabel helloLabel = new JLabel("Hello, " + IMDB.getInstance().getAccounts().get(userIndex).getUsername() + "(" +
                IMDB.getInstance().getAccounts().get(userIndex).getType() + ")");
        panel.add(helloLabel, BorderLayout.WEST);

        // experience button
        JButton experienceButton = new JButton("Experience");
        // open a message dialog to show the user's experience
        experienceButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Your experience is: " + IMDB.getInstance().getAccounts().get(userIndex).getExperience());
        });
        panel.add(experienceButton, BorderLayout.EAST);

        // recommendation message
        JLabel recommendationLabel = new JLabel("Recommended for you:");
        panel.add(recommendationLabel, BorderLayout.CENTER);

        // get top 5 productions as recommendations
        List<Production> topProductions = getTopRatedProductions(5);
        JPanel recommendationsPanel = new JPanel();
        recommendationsPanel.setLayout(new BoxLayout(recommendationsPanel, BoxLayout.X_AXIS));
        for (Production production : topProductions) {
            JLabel productionLabel = new JLabel(production.getTitle() + " (" + production.getAverageRating() + "/10)");
            recommendationsPanel.add(productionLabel);
        }
        panel.add(recommendationsPanel, BorderLayout.CENTER);

        // Search button
        JButton searchButton = new JButton("Search");
        JPanel searchPanel = SearchPanelGUI.createSearchPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(searchPanel, "searchPanel");
        searchButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "searchPanel");
        });
        panel.add(searchButton, BorderLayout.WEST);


        // Menu button
        JButton menuButton = new JButton("Menu");
        JPanel menuPanel = MenuGUI.createMenuPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(menuPanel, "menuPanel");
        menuButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "menuPanel");
        });
        panel.add(menuButton, BorderLayout.EAST);

        // logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            try {
                new LoginGUI();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        panel.add(logoutButton, BorderLayout.SOUTH);

        return panel;
    }

    private List<Production> getTopRatedProductions(int n) {
        return IMDB.getInstance().getProductions().stream()
                .sorted(Comparator.comparingDouble(Production::getAverageRating).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
}
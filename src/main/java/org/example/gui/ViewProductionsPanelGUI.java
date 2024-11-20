package org.example.gui;

import org.example.IMDB;
import org.example.Production;
import org.example.ProductionType;
import org.example.utils.DurationConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ViewProductionsPanelGUI extends JFrame {
    private static List<Production> productions;

    public static JPanel createViewProductionsPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        JPanel viewProductionsPanel = new JPanel(new BorderLayout());

        // Retrieve the productions list
        productions = IMDB.getInstance().getProductions();

        // Sort the productions list alphabetically
        Collections.sort(productions, Comparator.comparing(Production::getTitle));

        // Create the JList
        JList<Production> list = new JList<>(productions.toArray(new Production[0]));

        // Set the cell renderer
        list.setCellRenderer(new ListCellRenderer<Production>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Production> list, Production production, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add space between each production

                JLabel titleLabel = new JLabel("<html>" + (index + 1) + ". " + production.getTitle() + "</html>"); // Add number in front of the title
                titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Make the title bigger and bolder
                panel.add(titleLabel, BorderLayout.NORTH);

                JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel yearLabel = new JLabel("<html>" + production.getReleaseYear() + "&nbsp;&nbsp;&nbsp;&nbsp;" + "</html>");
                yearLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the year smaller
                detailsPanel.add(yearLabel);

                JLabel typeLabel = new JLabel("<html>" + production.getType() + "&nbsp;&nbsp;&nbsp;&nbsp;" + "</html>");
                typeLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the type smaller
                detailsPanel.add(typeLabel);

                if (production.type == ProductionType.Movie) {
                    String duration = DurationConverter.convertDuration(production.getDuration());
                    JLabel durationLabel = new JLabel("<html>" + duration + "&nbsp;&nbsp;&nbsp;&nbsp;" + "</html>");
                    durationLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the duration smaller
                    detailsPanel.add(durationLabel);
                } else {
                    JLabel numberOfSeasonsLabel = new JLabel("<html>" + production.getSeasons().size() + " seasons&nbsp;&nbsp;&nbsp;&nbsp;" + "</html>");
                    numberOfSeasonsLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the number of seasons smaller
                    detailsPanel.add(numberOfSeasonsLabel);
                }

                JLabel ratingLabel = new JLabel("<html>" + "Rating: " + production.getAverageRating() + "&nbsp;&nbsp;&nbsp;&nbsp;" + "</html>");
                ratingLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the rating smaller
                detailsPanel.add(ratingLabel);

                int reviews = 0;
                if (production.getRatings() != null) {
                    reviews = production.getRatings().size();
                }
                JLabel reviewsLabel = new JLabel("<html>" + "Reviews: " + reviews + "&nbsp;&nbsp;&nbsp;&nbsp;" + "</html>");
                reviewsLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the review count smaller
                detailsPanel.add(reviewsLabel);

                panel.add(detailsPanel, BorderLayout.CENTER);

                return panel;
            }
        });

        // Add the mouse listener
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    Production production = list.getSelectedValue();
                    JPanel productionPanel = ProductionPanelGUI.createProductionPanel(userIndex, cardPanel, cardLayout, production);
                    cardPanel.add(productionPanel, "productionPanel");
                    cardLayout.show(cardPanel, "productionPanel");
                }
            }
        });

        // Add the JList to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(list);
        list.setVisibleRowCount(10);

        // Set smooth scrolling
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16); // Adjust the increment value as needed for smoother scrolling

        // Create a panel for the sorting option
        JPanel sortPanel = createSortingPanel(list);

        // Add the JScrollPane to the panel
        viewProductionsPanel.add(sortPanel, BorderLayout.NORTH);
        viewProductionsPanel.add(scrollPane, BorderLayout.CENTER);

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(e -> {
            viewProductionsPanel.setVisible(false);
            cardLayout.show(cardPanel, "menuPanel");
        });

        viewProductionsPanel.add(goBackButton, BorderLayout.SOUTH);
        return viewProductionsPanel;
    }

    // Method to create the sorting panel with buttons
    private static JPanel createSortingPanel(JList<Production> list) {
        JPanel sortingPanel = new JPanel();

        // Button to sort by the number of reviews
        JButton sortByReviewsButton = new JButton("Sort by Reviews");
        sortByReviewsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortByReviews();
                list.setListData(productions.toArray(new Production[0]));  // Update the JList data
            }
        });

        // Button to sort by production type
        JButton sortByTypeButton = new JButton("Sort by Production Type");
        sortByTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortByProductionType();
                list.setListData(productions.toArray(new Production[0]));  // Update the JList data
            }
        });

        // Add buttons to the sorting panel
        sortingPanel.add(sortByReviewsButton);
        sortingPanel.add(sortByTypeButton);

        return sortingPanel;
    }

    private static void sortByReviews() {
        productions.sort(Collections.reverseOrder(Comparator.comparingInt(o -> Optional.ofNullable(o.getRatings()).orElse(Collections.emptyList()).size())));
    }

    // Method to sort the list by production type
    private static void sortByProductionType() {
        productions.sort(Comparator.comparing(Production::getType));
    }
}

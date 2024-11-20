package org.example.gui;

import org.example.AccountType;
import org.example.IMDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class MenuGUI extends JFrame {
    public static JPanel createMenuPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Display the name of the user at the top left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Align to the top left
        JLabel nameLabel = new JLabel("Welcome, " + org.example.IMDB.getInstance().getAccounts().get(userIndex).getName() + "!");
        panel.add(nameLabel, gbc);

        // Display user type just under the name
        gbc.gridy = 1;
        JLabel typeLabel = new JLabel("User type: " + org.example.IMDB.getInstance().getAccounts().get(userIndex).getType());
        panel.add(typeLabel, gbc);


        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "mainPagePanel");
        });
        panel.add(goBackButton, gbc);

        // Create buttons based on user type
        JButton viewProductionsButton = new JButton("View Productions"); // Vizualizarea detaliilor tuturor productiilor din sistem
        JButton viewActorsButton = new JButton("View Actors"); // Vizualizarea detaliilor tuturor actorilor din sistem
        JButton viewNotificationsButton = new JButton("View Notifications"); // Vizualizarea notificarilor primite
        JButton searchButton = new JButton("Search"); // Cautarea unui anumit film/serial/actor
        JButton favoritesButton = new JButton("Manage Favorites"); // Adaugarea/Stergerea unei productii/actor din lista de favorite
        JButton requestsButton = new JButton("Requests"); // Crearea/Retragerea unei cereri
        JButton manageProductionsOrActorsButton = new JButton("Manage Productions/Actors"); // Adaugarea/Stergerea unei productii/actor din sistem
        JButton solveRequestsButton = new JButton("Solve Requests"); // Vizualizarea si rezolvarea cererilor primite
        JButton updateProductionsOrActorsButton = new JButton("Update Productions/Actors"); // Actualizarea informatiilor despre productii/actori
        JButton manageUsersButton = new JButton("Manage Users"); // Adaugarea/Stergerea unui utilizator din sistem
        JButton logoutButton = new JButton("Logout"); // Delogare

        // Add buttons based on user type
        AccountType userType = IMDB.getInstance().getAccounts().get(userIndex).getType();
        switch (userType) {
            case Admin:
                addAdminButtons(panel, gbc, viewProductionsButton, viewActorsButton, viewNotificationsButton,
                        searchButton, favoritesButton, manageProductionsOrActorsButton, solveRequestsButton,
                        updateProductionsOrActorsButton, manageUsersButton, logoutButton);
                break;
            case Contributor:
                addContributorButtons(panel, gbc, viewProductionsButton, viewActorsButton, viewNotificationsButton,
                        searchButton, favoritesButton, requestsButton, solveRequestsButton, manageProductionsOrActorsButton,
                        updateProductionsOrActorsButton, logoutButton);
                break;
            case Regular:
                addRegularButtons(panel, gbc, viewProductionsButton, viewActorsButton, viewNotificationsButton,
                        searchButton, favoritesButton, requestsButton, logoutButton);
                break;
            default:
                // Handle unknown user type
                break;
        }


        JPanel viewProductionsPanel = ViewProductionsPanelGUI.createViewProductionsPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(viewProductionsPanel, "viewProductionsPanel");

        JPanel viewActorsPanel = ViewActorsPanelGUI.createViewActorsPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(viewActorsPanel, "viewActorsPanel");

        JPanel viewNotificationsPanel = ViewNotificationsPanelGUI.createViewNotificationsPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(viewNotificationsPanel, "viewNotificationsPanel");

        JPanel searchPanel = SearchPanelGUI.createSearchPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(searchPanel, "searchPanel");

        JPanel favoritesPanel = FavoritesPanelGUI.createFavoritesPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(favoritesPanel, "favoritesPanel");

        JPanel requestsPanel = RequestsPanelGUI.createRequestsPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(requestsPanel, "requestsPanel");

        JPanel manageProductionsOrActorsPanel = ManageProductionsOrActorsPanelGUI.createManageProductionsOrActorsPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(manageProductionsOrActorsPanel, "manageProductionsOrActorsPanel");

        JPanel solveRequestsPanel = SolveRequestsPanelGUI.createSolveRequestsPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(solveRequestsPanel, "solveRequestsPanel");

        JPanel updateProductionsOrActorsPanel = UpdateProductionsOrActorsPanelGUI.createUpdateProductionsOrActorsPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(updateProductionsOrActorsPanel, "updateProductionsOrActorsPanel");

        JPanel manageUsersPanel = ManageUsersPanelGUI.createManageUsersPanel(userIndex, cardPanel, cardLayout);
        cardPanel.add(manageUsersPanel, "manageUsersPanel");

        viewProductionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "viewProductionsPanel");
            }
        });

        viewActorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "viewActorsPanel");
            }
        });

        viewNotificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "viewNotificationsPanel");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "searchPanel");
            }
        });

        favoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "favoritesPanel");
            }
        });

        requestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "requestsPanel");
            }
        });

        manageProductionsOrActorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "manageProductionsOrActorsPanel");
            }
        });

        solveRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "solveRequestsPanel");
            }
        });

        updateProductionsOrActorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "updateProductionsOrActorsPanel");
            }
        });

        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "manageUsersPanel");
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardPanel.removeAll();
                try {
                    new LoginGUI();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return panel;
    }

    public static void showWelcomePanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        cardLayout.show(cardPanel, "welcomePanel");
    }

    private static void addAdminButtons(JPanel panel, GridBagConstraints gbc, JButton... buttons) {
        addButtons(panel, gbc, buttons);
    }

    private static void addContributorButtons(JPanel panel, GridBagConstraints gbc, JButton... buttons) {
        addButtons(panel, gbc, buttons);
    }

    private static void addRegularButtons(JPanel panel, GridBagConstraints gbc, JButton... buttons) {
        addButtons(panel, gbc, buttons);
    }

    private static void addButtons(JPanel panel, GridBagConstraints gbc, JButton... buttons) {
        int row = 2; // Start adding buttons from the third row
        for (JButton button : buttons) {
            gbc.gridx = 0;
            gbc.gridy = row++;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.insets = new Insets(10, 0, 0, 0); // Add vertical spacing
            panel.add(button, gbc);
        }
    }
}

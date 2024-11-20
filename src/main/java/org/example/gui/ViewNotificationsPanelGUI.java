package org.example.gui;

import org.example.AccountType;
import org.example.IMDB;
import org.example.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Map;

public class ViewNotificationsPanelGUI {
    public static JPanel createViewNotificationsPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        JPanel viewNotificationsPanel = new JPanel();
        viewNotificationsPanel.setLayout(new BorderLayout());

        // Get the current user
        User currentUser = IMDB.getInstance().getAccounts().get(userIndex);

        // Get the notifications for the current user
        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (currentUser.getNotifications() != null) {
            for(int i = 0; i < currentUser.getNotifications().size(); i++) {
                listModel.addElement((String) currentUser.getNotifications().get(i));
            }
        }

        JList<String> notificationsList = new JList<>(listModel);
        notificationsList.setCellRenderer(new ListCellRenderer<>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel(new BorderLayout());
                JLabel label = new JLabel(value);
                label.setFont(new Font("Arial", Font.BOLD, 14)); // Set the font size to 14
                panel.add(label, BorderLayout.CENTER);

                // Add a mouse listener to the label for deletion on click
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Remove the notification from the user's notifications
                        currentUser.getNotifications().remove(value);
                        // Remove the notification from the list model
                        listModel.remove(index);
                    }
                });

                return panel;
            }
        });

        notificationsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Only trigger on single clicks
                    int index = notificationsList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        // Remove the notification from the user's notifications
                        String notification = listModel.get(index);
                        int idx = 0;
                        for (int i = 0; i < currentUser.getNotifications().size(); i++) {
                            if (Objects.equals(currentUser.getNotifications().get(i), notification)) {
                                idx = i;
                                break;
                            }
                        }
                        currentUser.removeNotification(idx);
                        // Remove the notification from the list model
                        listModel.remove(index);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(notificationsList);
        viewNotificationsPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "menuPanel");
        });
        viewNotificationsPanel.add(backButton, BorderLayout.SOUTH);

        return viewNotificationsPanel;
    }
}

package org.example.gui;

import org.example.AccountType;
import org.example.IMDB;
import org.example.Request;
import org.example.RequestsHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SolveRequestsPanelGUI {
    public static JList<Request> list;
    public static JPanel createSolveRequestsPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        JPanel requestsPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Solve Requests", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 50)); // Increase the font size
        requestsPanel.add(titleLabel, BorderLayout.NORTH);

        // check if im admin
        if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Admin) {
            RequestsHolder.loadAdminRequests();
            List<Request> adminRequests = RequestsHolder.getAdminRequestsList();

            DefaultListModel<Request> listModel = new DefaultListModel<>();
            for (Request request : adminRequests) {
                listModel.addElement(request);
            }

            List<Request> myRequests = new ArrayList<>();
            for (Request request : IMDB.getInstance().getRequests()) {
                if (request.getTo().equals(IMDB.getInstance().getAccounts().get(userIndex).getUsername())) {
                    myRequests.add(request);
                }
            }

            for (Request request : myRequests) {
                listModel.addElement(request);
            }


            // Create the JList
            list = new JList<>(listModel);

            // Set the cell renderer
            list.setCellRenderer(new MyListCellRenderer());

            // Set the cell height
            list.setFixedCellHeight(70); // Increase the cell height

            // Add a mouse motion listener to update the hovered index and repaint the list
            list.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int index = list.locationToIndex(e.getPoint());
                    ((MyListCellRenderer) list.getCellRenderer()).setHoveredIndex(index);
                    list.repaint();
                }
            });

            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        Request request = list.getSelectedValue();
                        JPanel requestSolverPanel = RequestPagePanel.createRequestPagePanel(userIndex, cardPanel, cardLayout, request, 2);
                        cardPanel.add(requestSolverPanel, "requestPagePanel");
                        cardLayout.show(cardPanel, "requestPagePanel");
                    }
                }
            });

            // Add the JList to a JScrollPane
            JScrollPane scrollPane = new JScrollPane(list);
            requestsPanel.add(scrollPane, BorderLayout.CENTER);
        } else if (IMDB.getInstance().getAccounts().get(userIndex).getType() == AccountType.Contributor) {
            List<Request> myRequests = new ArrayList<>();
            for (Request request : IMDB.getInstance().getRequests()) {
                if (request.getTo().equals(IMDB.getInstance().getAccounts().get(userIndex).getUsername())) {
                    myRequests.add(request);
                }
            }

            DefaultListModel<Request> listModel = new DefaultListModel<>();
            for (Request request : myRequests) {
                listModel.addElement(request);
            }
            // Create the JList
            list = new JList<>(listModel);

            // Set the cell renderer
            list.setCellRenderer(new MyListCellRenderer());

            // Set the cell height
            list.setFixedCellHeight(70); // Increase the cell height

            // Add a mouse motion listener to update the hovered index and repaint the list
            list.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int index = list.locationToIndex(e.getPoint());
                    ((MyListCellRenderer) list.getCellRenderer()).setHoveredIndex(index);
                    list.repaint();
                }
            });

            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        Request request = list.getSelectedValue();
                        JPanel requestSolverPanel = RequestPagePanel.createRequestPagePanel(userIndex, cardPanel, cardLayout, request, 2);
                        cardPanel.add(requestSolverPanel, "requestPagePanel");
                        cardLayout.show(cardPanel, "requestPagePanel");
                    }
                }
            });

            // Add the JList to a JScrollPane
            JScrollPane scrollPane = new JScrollPane(list);
            requestsPanel.add(scrollPane, BorderLayout.CENTER);

        }
        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "menuPanel");
        });
        requestsPanel.add(backButton, BorderLayout.SOUTH);
        return requestsPanel;
    }

    public static String createdDateConverter(LocalDateTime createdDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' hh:mm a");
        return createdDate.format(formatter);
    }

    public static void removeRequestFromList(Request request) {
        // remove element from list
        DefaultListModel<Request> model = (DefaultListModel<Request>) list.getModel();
        model.removeElement(request);

        // update the list
        list.setModel(model);

        // update the list cell renderer
        list.setCellRenderer(new MyListCellRenderer());
    }
}

class MyListCellRenderer extends DefaultListCellRenderer {
    private int hoveredIndex = -1;

    public void setHoveredIndex(int index) {
        this.hoveredIndex = index;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (index == hoveredIndex) {
            c.setBackground(new Color(245,245,245)); // Change the color to grey
        } else {
            c.setBackground(list.getBackground());
        }

        Request request = (Request) value;
        setText("<html><font size=\"5\">" + request.getRequestType() + "<br/>" + request.getUsername() + "<br/>" + SolveRequestsPanelGUI.createdDateConverter(request.getCreatedDate()) + "</font></html>"); // Increase the font size

        return c;
    }
}
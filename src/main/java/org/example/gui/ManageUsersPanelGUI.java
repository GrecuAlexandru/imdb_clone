package org.example.gui;

import org.example.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.SecureRandom;
import java.time.LocalDateTime;

public class ManageUsersPanelGUI {
    public static JPanel createManageUsersPanel(int userIndex, JPanel cardPanel, CardLayout cardLayout) {
        JPanel manageUsersPanel = new JPanel();
        manageUsersPanel.setLayout(new BorderLayout());

        // Create a list model
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Add items to the list model
        for (User user : IMDB.getInstance().getAccounts()) {
            listModel.addElement(user.getUsername());
        }

        // Create a list and add it to a scroll pane
        JList<String> usersList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(usersList);
        manageUsersPanel.add(scrollPane, BorderLayout.CENTER);

        // Add a MouseListener to the users list
        usersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click detected
                    int index = usersList.locationToIndex(e.getPoint());

                    Object[] options = {"Delete User", "Update User"};

                    int choice = JOptionPane.showOptionDialog(null,
                            "What do you want to do with the user?",
                            "User Management",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if (choice == JOptionPane.NO_OPTION) {
                        String oldUsername = listModel.getElementAt(index);
                        String newUsername = JOptionPane.showInputDialog("Enter the new username for the user:", oldUsername);
                        User oldUser = IMDB.getInstance().getAccounts().get(index);
                        String oldEmail = oldUser.getEmail();
                        String newEmail = JOptionPane.showInputDialog("Enter the new email for the user:", oldEmail);
                        String oldPassword = oldUser.getPassword();
                        String newPassword = JOptionPane.showInputDialog("Enter the new password for the user:", oldPassword);
                        String oldCountry = oldUser.getCountry();
                        String newCountry = JOptionPane.showInputDialog("Enter the new country for the user:", oldCountry);
                        String oldGender = oldUser.getGender();
                        String newGender = JOptionPane.showInputDialog("Enter the new gender for the user:", oldGender);
                        String oldDateOfBirth = oldUser.getBirthDate().toString();
                        String newDateOfBirth = JOptionPane.showInputDialog("Enter the new date of birth for the user:", oldDateOfBirth);
                        String oldAge = String.valueOf(oldUser.getAge());
                        String newAge = JOptionPane.showInputDialog("Enter the new age for the user:", oldAge);
                        String oldName = oldUser.getName();
                        String newName = JOptionPane.showInputDialog("Enter the new name for the user:", oldName);

                        Credentials newCredentials = new Credentials(newEmail, newPassword);
                        User.Information info = new User.Information.InformationBuilder(newCredentials)
                                .name(newName)
                                .country(newCountry)
                                .age(Integer.valueOf(newAge))
                                .gender(newGender)
                                .birthDate(LocalDateTime.parse(newDateOfBirth))
                                .build();

                        User newUser = UserFactory.createUser(oldUser.getType(), info, newUsername, oldUser.getExperience(), oldUser.getNotifications(), oldUser.getFavorites(), null);

                        Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
                        admin.updateUserInSystem(oldUsername, newUser);
                    } else if (choice == JOptionPane.YES_OPTION) {
                        Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
                        admin.removeUserFromSystem(IMDB.getInstance().getAccounts().get(index));
                    }
                }
            }
        });

        // Add new user button
        JButton addNewUserButton = new JButton("Add new User");
        addNewUserButton.setPreferredSize(new Dimension(300, 50)); // Set the preferred size of the button
        addNewUserButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter the name for the user:");
            JTextField usernameField = new JTextField(generateUsername(name));
            JTextField passwordField = new JTextField(generatePassword());

            // Create a button for generating a new username and password
            JButton generateButton = new JButton("Generate new username and password");
            generateButton.addActionListener(ev -> {
                usernameField.setText(generateUsername(name));
                passwordField.setText(generatePassword());
            });

            // Add the fields and button to a panel
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Username:"));
            panel.add(usernameField);
            panel.add(new JLabel("Password:"));
            panel.add(passwordField);
            panel.add(generateButton);

            // Show the panel in a dialog
            int result = JOptionPane.showConfirmDialog(null, panel, "Enter user details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                // Rest of the code remains the same
                String email = JOptionPane.showInputDialog("Enter the email for the user:");
                String country = JOptionPane.showInputDialog("Enter the country for the user:");
                String gender = JOptionPane.showInputDialog("Enter the gender for the user:");
                String age = JOptionPane.showInputDialog("Enter the age for the user:");
                String accountType = JOptionPane.showInputDialog("Enter the account type for the user (Admin, Contributor, Regular):");
                String dateOfBirth = JOptionPane.showInputDialog("Enter the date of birth for the user (1981-04-08T00:00):");
                AccountType type = AccountType.fromString(accountType);

                Credentials credentials = new Credentials(email, password);
                User.Information info = new User.Information.InformationBuilder(credentials)
                        .name(name)
                        .country(country)
                        .age(Integer.valueOf(age))
                        .gender(gender)
                        .birthDate(LocalDateTime.parse(dateOfBirth))
                        .build();

                User newUser = UserFactory.createUser(type, info, username, 0, null, null, null);

                Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
                admin.addUserToSystem(newUser);
            }

        });
        manageUsersPanel.add(addNewUserButton, BorderLayout.SOUTH);

        // Back button
        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "mainPagePanel");
        });
        manageUsersPanel.add(goBackButton, BorderLayout.EAST);

        return manageUsersPanel;
    }

    private static String generateUsername(String name) {
        String username = name.replace(" ", "_").toLowerCase();
        int randomNum = new SecureRandom().nextInt(1000);
        return username + "_" + randomNum;
    }

    private static String generatePassword() {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }
}

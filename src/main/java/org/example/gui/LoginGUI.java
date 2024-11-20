package org.example.gui;

import com.sun.tools.javac.Main;
import org.example.IMDB;
import org.example.utils.loginRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class LoginGUI extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel titleLabel = new JLabel("LOG IN", SwingConstants.CENTER); // Add this line
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JCheckBox showPassword = new JCheckBox("Show Password");

    public LoginGUI() throws FileNotFoundException {
        IMDB.getInstance().parseAll();
        setTitle("IMDB - LOGIN");
        setSize(450,450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        titleLabel.setBounds(60, 50, 300, 50); // Add this line
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 36)); // Add this line
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 250, 30);
        passwordField.setBounds(150, 220, 250, 30);
        showPassword.setBounds(150, 250, 250, 30);
        loginButton.setBounds(50, 300, 350, 30);
    }

    public void addComponentsToContainer() {
        container.add(titleLabel); // Add this line
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
    }

    public void addActionEvent() {
        loginButton.addActionListener(this);
        showPassword.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //Coding Part of LOGIN button
        if (e.getSource() == loginButton) {
            String userText;
            String pwdText;
            userText = userTextField.getText();
            pwdText = passwordField.getText();

            int userIndex = loginRequest.loginRequest(userText, pwdText);
            if (userIndex != -1) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                this.dispose();
                new MainPagePanelGUI(userIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        }
        //Coding Part of showPassword JCheckBox
        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        }
    }
}
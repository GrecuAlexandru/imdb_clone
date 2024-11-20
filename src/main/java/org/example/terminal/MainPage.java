package org.example.terminal;

import org.example.IMDB;
import org.example.User;

import java.io.FileNotFoundException;

public class MainPage {
    public static void mainPage(int userIndex) throws FileNotFoundException {
        User user = IMDB.getInstance().getAccounts().get(userIndex);
        System.out.println("Welcome back user " + user.getUsername());
        System.out.println("Username: " + user.getUsername());
        System.out.println("User experience: " + user.getExperience());
        System.out.println("User type: " + user.getType());
        Menu.menu(userIndex);
    }
}

package org.example.terminal;

import org.example.IMDB;
import org.example.utils.loginRequest;

import java.io.Console;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Login {
    public static void login() throws FileNotFoundException {
        IMDB.getInstance().parseAll();

        Scanner scanner = new Scanner(System.in);

        System.out.println("LOG IN");

        System.out.print("EMAIL: ");
        String email = scanner.nextLine();

        System.out.print("PASSWORD: ");
        String password = scanner.nextLine();

        int userIndex = loginRequest.loginRequest(email, password);
        if (userIndex != -1) {
            System.out.println("Login Successful");
            Menu.menu(userIndex);
        } else {
            System.out.println("Invalid Username or Password");
            login();
        }
    }
}

package org.example.terminal;

import org.example.*;

import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ManageUsers {
    public static void manageUsers(int userIndex) throws FileNotFoundException {
        System.out.println("Manage users");
        System.out.println("Choose action:");
        System.out.println("    1) Add user");
        System.out.println("    2) Delete user");
        System.out.println("    3) Go back");

        Scanner scanner = new Scanner(System.in);

        String input = scanner.next();

        switch(input) {
            case "1":
                addUser(userIndex, scanner);
                manageUsers(userIndex);
                break;
            case "2":
                deleteUser(userIndex, scanner);
                manageUsers(userIndex);
                break;
            case "3":
                Menu.menu(userIndex);
                break;
            default:
                System.out.println("Invalid input");
                manageUsers(userIndex);
        }
    }

    private static void deleteUser(int userIndex, Scanner scanner) {
        // print all users
        System.out.println("Choose the user you want to delete:");
        for (int i = 0; i < IMDB.getInstance().getAccounts().size(); i++) {
            if (i != userIndex) {
                System.out.println("    " + (i + 1) + ") " + IMDB.getInstance().getAccounts().get(i).getUsername());
            }
        }

        String input = scanner.next();
        int userToDeleteIndex = Integer.parseInt(input) - 1;
        if (userToDeleteIndex == userIndex) {
            System.out.println("You cannot delete yourself");
            deleteUser(userIndex, scanner);
        } else {
            Admin admin = (Admin) IMDB.getInstance().getAccounts().get(userIndex);
            admin.removeUserFromSystem(IMDB.getInstance().getAccounts().get(userToDeleteIndex));
        }

        System.out.println("User deleted");
    }

    private static void addUser(int userIndex, Scanner scanner) {
        System.out.println("Enter the name for the user:");
        String name = scanner.next();
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid name");
            addUser(userIndex, scanner);
        }
        String username = generateUsername(name);
        System.out.println("Generated username: " + username);

        String password = generatePassword();
        System.out.println("Generated password: " + password);

        System.out.println("Enter the email for the user:");
        String email = scanner.next();

        System.out.println("Enter the country for the user:");
        String country = scanner.next();

        System.out.println("Enter the gender for the user:");
        String gender = scanner.next();

        System.out.println("Enter the age for the user:");
        String age = scanner.next();

        System.out.println("Enter the account type for the user (Admin, Contributor, Regular):");
        String accountType = scanner.next();

        System.out.println("Enter the date of birth for the user (1981-04-08T00:00):");
        String dateOfBirth = scanner.next();

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

        System.out.println("User added");
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

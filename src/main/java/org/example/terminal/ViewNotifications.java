package org.example.terminal;

import org.example.IMDB;
import org.example.User;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class ViewNotifications {
    public static void viewNotifications(int userIndex) throws FileNotFoundException {
        System.out.println("Notifications");
        Scanner scanner = new Scanner(System.in);
        User currentuser = IMDB.getInstance().getAccounts().get(userIndex);

        if (currentuser.getNotifications() != null && currentuser.getNotifications().size() > 0) {
            for (Object notification : currentuser.getNotifications()) {
                System.out.println((String) notification);
            }
            System.out.println("Press 1 to delete all notifications");
            String input = scanner.next();
            if (input.equals("1")) {
                IMDB.getInstance().getAccounts().get(userIndex).getNotifications().clear();
                System.out.println("Notifications deleted");
            }
        } else {
            System.out.println("No new notifications");
        }

        System.out.println("Press x to go back");

        String input = scanner.next();
        if (input.equals("x")) {
            Menu.menu(userIndex);
        }
    }
}

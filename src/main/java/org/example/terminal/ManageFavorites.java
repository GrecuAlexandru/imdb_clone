package org.example.terminal;

import org.example.*;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.SortedSet;

public class ManageFavorites {
    public static void manageFavorites(int userIndex) throws FileNotFoundException {
        System.out.println("Manage favorites");

        User currentUser = IMDB.getInstance().getAccounts().get(userIndex);

        if (currentUser.getFavorites().size() == 0) {
            System.out.println("No favorites");
            Menu.menu(userIndex);
        } else {
            SortedSet<Comparable<?>> favorites = IMDB.getInstance().getAccounts().get(userIndex).getFavorites();
            int i = 1;
            for(Comparable<?> favorite : favorites) {
                if (favorite instanceof Actor) {
                    System.out.println("    " + i + ") " + ((Actor) favorite).getName());
                } else if (favorite instanceof Movie) {
                    System.out.println("    " + i + ") " + ((Movie) favorite).getTitle());
                } else if (favorite instanceof Series) {
                    System.out.println("    " + i + ") " + ((Series) favorite).getTitle());
                }
            }

            System.out.println("Enter the number of the favorite you want to remove (or type x to go back):");

            Scanner scanner = new Scanner(System.in);

            String input = scanner.next();

            if (input.equals("x")) {
                Menu.menu(userIndex);
            } else {
                int favoriteIndex = Integer.parseInt(input) - 1;
                Comparable<?> production = (Comparable<?>) favorites.toArray()[favoriteIndex];
                IMDB.getInstance().getAccounts().get(userIndex).removeFavorite((Comparable<?>) production);
                System.out.println("Favorite removed");
                manageFavorites(userIndex);
            }
        }
    }
}

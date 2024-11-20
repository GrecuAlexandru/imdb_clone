package org.example.terminal;

import org.example.Actor;
import org.example.IMDB;
import org.example.Production;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Search {
    public static void search(int userIndex) throws FileNotFoundException {
        System.out.println("Search");
        System.out.println("Enter the name of the actor/movie/series you want to search for (or type x to go back):");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        if (input.equals("x")) {
            Menu.menu(userIndex);
        } else {
            performSearch(input);
            search(userIndex);
        }
    }

    private static void performSearch(String searchField) {
        String query = searchField.toLowerCase();
        StringBuilder results = new StringBuilder();

        for (Production production : IMDB.getInstance().getProductions()) {
            if (production.getTitle().toLowerCase().contains(query)) {
                results.append("Production: ").append(production.getTitle()).append("\n");
            }
        }

        for (Actor actor : IMDB.getInstance().getActors()) {
            if (actor.getName().toLowerCase().contains(query)) {
                results.append("Actor: ").append(actor.getName()).append("\n");
            }
        }

        System.out.println(results);
    }
}

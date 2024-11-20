package org.example.terminal;

import org.example.Actor;
import org.example.IMDB;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class ViewActors {
    private static List<Actor> actors;

    public static void viewActors(int userIndex) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Actors");

        actors = IMDB.getInstance().getActors();

        for (int i = 0; i < actors.size(); i++) {
            System.out.println(i + 1 + ". " + actors.get(i).getName());
        }

        System.out.println("Enter the number of the actor you want to view details about (or type x to go back):");

        String input = scanner.next();
        if (input.equals("x")) {
            Menu.menu(userIndex);
        } else {
            int actorIndex = Integer.parseInt(input) - 1;
            ActorPage.actorPage(userIndex, actors.get(actorIndex));
        }
    }
}

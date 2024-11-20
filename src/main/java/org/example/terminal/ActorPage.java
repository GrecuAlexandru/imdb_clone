package org.example.terminal;

import org.example.Actor;
import org.example.IMDB;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class ActorPage {
    public static void actorPage(int userIndex, Actor actor) throws FileNotFoundException {
        System.out.println(actor.getName());
        System.out.println("Biography: " + actor.getBiography());
        System.out.println("Performances: ");
        for (Actor.Performance performance : actor.getPerformances()) {
            System.out.println(performance.getTitle() + " (" + performance.getType() + ")");
        }

        System.out.println("Choose action:");
        if (IMDB.getInstance().getAccounts().get(userIndex).getFavorites().contains(actor)) {
            System.out.println("    1) Remove from favorites");
        } else {
            System.out.println("    1) Add to favorites");
        }
        System.out.println("Press x to go back");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        if (input.equals("x")) {
            ViewActors.viewActors(userIndex);
        } else if (input.equals("1")) {
            if (IMDB.getInstance().getAccounts().get(userIndex).getFavorites().contains(actor)) {
                IMDB.getInstance().getAccounts().get(userIndex).removeFavorite(actor);
                System.out.println("Removed from favorites");
            } else {
                IMDB.getInstance().getAccounts().get(userIndex).addFavorite(actor);
                System.out.println("Added to favorites");
            }
            actorPage(userIndex, actor);
        }
    }
}

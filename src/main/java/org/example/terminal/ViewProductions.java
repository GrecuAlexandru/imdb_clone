package org.example.terminal;

import org.example.IMDB;
import org.example.Production;

import java.io.FileNotFoundException;
import java.util.*;

public class ViewProductions {
    private static List<Production> productions;

    public static void viewProductions(int userIndex) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Productions");
        // choose sorting method
        System.out.println("Choose sorting method:");
        System.out.println("    1) Sort by name");
        System.out.println("    2) Sort by production type");
        System.out.println("    3) Sort by number of reviews");
        System.out.println("    4) Go back");

        int choice = scanner.nextInt();

        productions = IMDB.getInstance().getProductions();

        Collections.sort(productions, Comparator.comparing(Production::getTitle));

        switch (choice) {
            case 1:
                // sort by name
                for (int i = 0; i < productions.size(); i++) {
                    if (productions.get(i).getRatings() != null) {
                        System.out.println(i + 1 + ". " + productions.get(i).getTitle()
                                + " (" + productions.get(i).getType() + ", reviews: "
                                + productions.get(i).getRatings().size() + ")");
                    } else {
                        System.out.println(i + 1 + ". " + productions.get(i).getTitle()
                                + " (" + productions.get(i).getType() + ", reviews: 0)");
                    }
                }
                System.out.println("Enter the number of the production you want to view details about (or type x to go back):");
                String input = scanner.next();
                if (input.equals("x")) {
                    Menu.menu(userIndex);
                } else {
                    int productionIndex = Integer.parseInt(input) - 1;
                    ProductionPage.productionPage(userIndex, productions.get(productionIndex));
                }
                break;
            case 2:
                // sort by production type
                sortByProductionType();
                for (int i = 0; i < productions.size(); i++) {
                    if (productions.get(i).getRatings() != null) {
                        System.out.println(i + 1 + ". " + productions.get(i).getTitle()
                                + " (" + productions.get(i).getType() + ", reviews: "
                                + productions.get(i).getRatings().size() + ")");
                    } else {
                        System.out.println(i + 1 + ". " + productions.get(i).getTitle()
                                + " (" + productions.get(i).getType() + ", reviews: 0)");
                    }
                }
                break;
            case 3:
                // sort by number of reviews
                sortByReviews();
                for (int i = 0; i < productions.size(); i++) {
                    if (productions.get(i).getRatings() != null) {
                        System.out.println(i + 1 + ". " + productions.get(i).getTitle()
                                + " (" + productions.get(i).getType() + ", reviews: "
                                + productions.get(i).getRatings().size() + ")");
                    } else {
                        System.out.println(i + 1 + ". " + productions.get(i).getTitle()
                                + " (" + productions.get(i).getType() + ", reviews: 0)");
                    }
                }
                break;
            case 4:
                Menu.menu(userIndex);
                break;
            default:
                System.out.println("Invalid choice. Please enter 1, 2 or 3.");
                viewProductions(userIndex);
                break;
        }
    }

    private static void sortByReviews() {
        productions.sort(Collections.reverseOrder(Comparator.comparingInt(o -> Optional.ofNullable(o.getRatings()).orElse(Collections.emptyList()).size())));
    }
    // Method to sort the list by production type
    private static void sortByProductionType() {
        productions.sort(Comparator.comparing(Production::getType));
    }

}

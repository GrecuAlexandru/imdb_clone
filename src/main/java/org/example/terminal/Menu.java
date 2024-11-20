package org.example.terminal;

import org.example.AccountType;
import org.example.IMDB;
import org.example.User;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {
    public static void menu(int userIndex) throws FileNotFoundException {
        User user = IMDB.getInstance().getAccounts().get(userIndex);

        Scanner scanner = new Scanner(System.in);

        if (user.getType() == AccountType.Admin) {
            System.out.println("Choose action:");
            System.out.println("    1) View productions details");
            System.out.println("    2) View actors details");
            System.out.println("    3) View notifications");
            System.out.println("    4) Search for actor/movie/series");
            System.out.println("    5) Add/Delete actor/movie/series to/from favorites");
            System.out.println("    6) Add/Delete user");
            System.out.println("    7) Add/Delete actor/movie/series from system");
            System.out.println("    8) Update Production/Actor details");
            System.out.println("    9) Solve a request");
            System.out.println("    10) Logout");

            int choice = scanner.nextInt();

            switch(choice) {
                case 1:
                    ViewProductions.viewProductions(userIndex);
                    break;
                case 2:
                    ViewActors.viewActors(userIndex);
                    break;
                case 3:
                    ViewNotifications.viewNotifications(userIndex);
                    break;
                case 4:
                    Search.search(userIndex);
                    break;
                case 5:
                    ManageFavorites.manageFavorites(userIndex);
                    break;
                case 6:
                    ManageUsers.manageUsers(userIndex);
                    break;
                case 7:
                    ManageProductionsOrActors.manageProductionsOrActors(userIndex);
                    break;
                case 8:
                    UpdateProductionsOrActors.updateProductionsOrActors(userIndex);
                    break;
                case 9:
                    SolveRequests.solveRequests(userIndex);
                    break;
                case 10:
                    System.out.println("Logout successful");
                    Login.login();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 11.");
                    menu(userIndex);
                    break;
            }
            ViewProductions.viewProductions(userIndex);
        } else if (user.getType() == AccountType.Contributor) {
            System.out.println("Choose action:");
            System.out.println("    1) View productions details");
            System.out.println("    2) View actors details");
            System.out.println("    3) View notifications");
            System.out.println("    4) Search for actor/movie/series");
            System.out.println("    5) Add/Delete actor/movie/series to/from favorites");
            System.out.println("    6) Add/Delete actor/movie/series from system");
            System.out.println("    7) Update Production/Actor details");
            System.out.println("    8) Create/Delete a request");
            System.out.println("    9) Solve a request");
            System.out.println("    10) Logout");

            int choice = scanner.nextInt();

            switch(choice) {
                case 1:
                    ViewProductions.viewProductions(userIndex);
                    break;
                case 2:
                    ViewActors.viewActors(userIndex);
                    break;
                case 3:
                    ViewNotifications.viewNotifications(userIndex);
                    break;
                case 4:
                    Search.search(userIndex);
                    break;
                case 5:
                    ManageFavorites.manageFavorites(userIndex);
                    break;
                case 6:
                    ManageProductionsOrActors.manageProductionsOrActors(userIndex);
                    break;
                case 7:
                    UpdateProductionsOrActors.updateProductionsOrActors(userIndex);
                    break;
                case 8:
                    Requests.requests(userIndex);
                    break;
                case 9:
                    SolveRequests.solveRequests(userIndex);
                    break;
                case 10:
                    System.out.println("Logout successful");
                    Login.login();
                    break;
            }
        } else if (user.getType() == AccountType.Regular) {
            System.out.println("Choose action:");
            System.out.println("    1) View productions details");
            System.out.println("    2) View actors details");
            System.out.println("    3) View notifications");
            System.out.println("    4) Search for actor/movie/series");
            System.out.println("    5) Add/Delete actor/movie/series to/from favorites");
            System.out.println("    6) Create/Delete a request");
            System.out.println("    7) Logout");

            int choice = scanner.nextInt();

            switch(choice) {
                case 1:
                    ViewProductions.viewProductions(userIndex);
                    break;
                case 2:
                    ViewActors.viewActors(userIndex);
                    break;
                case 3:
                    ViewNotifications.viewNotifications(userIndex);
                    break;
                case 4:
                    Search.search(userIndex);
                    break;
                case 5:
                    ManageFavorites.manageFavorites(userIndex);
                    break;
                case 6:
                    Requests.requests(userIndex);
                    break;
                case 7:
                    System.out.println("Logout successful");
                    Login.login();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                    menu(userIndex);
                    break;
            }
        }
    }
}

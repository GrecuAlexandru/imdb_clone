package org.example;

import java.util.List;
import java.util.SortedSet;
public class UserFactory {

        public static User createUser(AccountType accountType, User.Information info, String username, int experience, List<String> notifications, SortedSet<Comparable<?>> favorites, SortedSet<Comparable<?>> contributions) {
                switch (accountType) {
                        case Admin:
                                return new Admin(info, accountType, username, experience, notifications, favorites, contributions);
                        case Regular:
                                return new Regular(info, accountType, username, experience, notifications, favorites, contributions);
                        case Contributor:
                                return new Contributor(info, accountType, username, experience, notifications, favorites, contributions);
                        default:
                                throw new IllegalArgumentException("Unsupported account type: " + accountType);
                }
        }
}
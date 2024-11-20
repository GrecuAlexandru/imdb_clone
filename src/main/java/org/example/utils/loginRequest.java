package org.example.utils;

import org.example.IMDB;
import org.example.User;

public class loginRequest {
    public static int loginRequest(String email, String password) {
        IMDB imdb = IMDB.getInstance();
        for (User account : imdb.getAccounts()) {
            if (account.getEmail().equals(email) && account.getPassword().equals(password)) {
                return imdb.getAccounts().indexOf(account);
            }
        }
        return -1; // Return -1 if no matching user is found
    }
}

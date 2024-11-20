package org.example;

public enum AccountType {
    Regular, Contributor, Admin;

    public static AccountType fromString(String accountType) {
        switch (accountType) {
            case "Regular":
                return Regular;
            case "Contributor":
                return Contributor;
            case "Admin":
                return Admin;
            default:
                return null;
        }
    }
}

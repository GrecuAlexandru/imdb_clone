package org.example;

import org.example.utils.JsonParser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public abstract class User<T> implements Observer {
    public static class Information {
        private final Credentials credentials;
        private String name;
        private String country;
        private Integer age;
        private String gender;
        private LocalDateTime birthDate;

        public Information(InformationBuilder informationBuilder) {
            this.credentials = informationBuilder.credentials;
            this.name = informationBuilder.name;
            this.country = informationBuilder.country;
            this.age = informationBuilder.age;
            this.gender = informationBuilder.gender;
            this.birthDate = informationBuilder.birthDate;
        }

        public static class InformationBuilder {
            private final Credentials credentials;
            private String name;
            private String country;
            private Integer age;
            private String gender;
            private LocalDateTime birthDate;

            public InformationBuilder(
                    Credentials credentials) {
                this.credentials = credentials;
            }

            public InformationBuilder name(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder country(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder age(Integer age) {
                this.age = age;
                return this;
            }

            public InformationBuilder gender(String gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder birthDate(LocalDateTime birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }
    }

    private Information information;
    private AccountType userType;
    private String username;
    private Integer experience;
    private List<String> notifications;
    public SortedSet<Comparable<?>> favorites; // production or actors

    public void addFavorite(Comparable<?> favorite) {
        favorites.add(favorite);
        int userIndex = IMDB.getInstance().getAccounts().indexOf(this);
        JsonParser.addFavoritesToJson(userIndex, favorite);
    }

    public void removeFavorite(Comparable<?> favorite) {
        favorites.remove(favorite);
        int userIndex = IMDB.getInstance().getAccounts().indexOf(this);
        JsonParser.removeFavoritesFromJson(userIndex, favorite);
    }

    public SortedSet<Comparable<?>> getFavorites() {
        return favorites;
    }

    public User(
            Information information,
            AccountType userType,
            String username,
            Integer experience,
            List<String> notifications,
            SortedSet<Comparable<?>> favorites) {
        this.information = information;
        this.userType = userType;
        this.username = username;
        this.experience = experience;
        this.notifications = notifications;
        this.favorites = favorites;
    }

    public String getEmail() {
        return this.information.credentials.email;
    }

    public String getPassword() {
        return this.information.credentials.password;
    }

    public String getUsername() {
        return this.username;
    }

    public Integer getExperience() {
        return this.experience;
    }

    public String getName() {
        return this.information.name;
    }

    public String getCountry() {
        return this.information.country;
    }

    public Integer getAge() {
        return this.information.age;
    }

    public AccountType getType() {
        return this.userType;
    }

    public LocalDateTime getBirthDate() {
        return this.information.birthDate;
    }

    public String getGender() {
        return this.information.gender;
    }

    public List<String> getNotifications() {
        return this.notifications;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.information.credentials.email = email;
    }

    public void setPassword(String password) {
        this.information.credentials.password = password;
    }

    public void setCountry(String country) {
        this.information.country = country;
    }

    public void setAge(Integer age) {
        this.information.age = age;
    }

    public void setName(String name) {
        this.information.name = name;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.information.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.information.gender = gender;
    }

    public void removeNotification(int index) {
        this.notifications.remove(index);
        JsonParser.writeAccountsJson(IMDB.getInstance().getAccounts());
    }

    public void displayInfo() {
        System.out.println("Username: " + this.username);
        System.out.println("Name: " + this.information.name);
        System.out.println("Country: " + this.information.country);
        System.out.println("Age: " + this.information.age);
    }

    public void updateExperience(ExperienceStrategy experienceStrategy) {
        this.experience += experienceStrategy.calculateExperience();
        JsonParser.writeAccountsJson(IMDB.getInstance().getAccounts());
    }

    @Override
    public void update(Notification notification) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification.getMessage());
        JsonParser.writeAccountsJson(IMDB.getInstance().getAccounts());
    }
}
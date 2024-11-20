package org.example;


import org.example.utils.JsonParser;

import java.util.List;
import java.util.SortedSet;

public class Admin<T> extends Staff {
    public SortedSet<Comparable<Object>> contributions;
    public Admin(Information info,
                 AccountType accountType,
                 String username,
                 Integer experience,
                 List<String> notifications,
                 SortedSet<Comparable<Object>> favorites,
                 SortedSet<Comparable<Object>> contributions) {
        super(info, accountType, username, experience, notifications, favorites);
        this.contributions = contributions;
    }

    public SortedSet<Comparable<Object>> getContributions() {
        return contributions;
    }

    public boolean checkContribution(String contributionTitle) {
        if (contributions == null) {
            return false;
        }
        for (Comparable<Object> contribution : contributions) {
            if (contribution instanceof Production) {
                if (((Production) contribution).title.equals(contributionTitle)) {
                    return true;
                }
            } else if (contribution instanceof Actor) {
                if (((Actor) contribution).name.equals(contributionTitle)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Object getContribution(String contributionTitle) {
        if (contributions == null) {
            return null;
        }
        for (Comparable<Object> contribution : contributions) {
            if (contribution instanceof Production) {
                if (((Production) contribution).title.equals(contributionTitle)) {
                    return contribution;
                }
            } else if (contribution instanceof Actor) {
                if (((Actor) contribution).name.equals(contributionTitle)) {
                    return contribution;
                }
            }
        }
        return null;
    }

    public void addUserToSystem(User user) {
        IMDB.getInstance().getAccounts().add(user);
        JsonParser.writeAccountsJson(IMDB.getInstance().getAccounts());
    }

    public void removeUserFromSystem(User user) {
        IMDB.getInstance().getAccounts().remove(user);
        JsonParser.writeAccountsJson(IMDB.getInstance().getAccounts());
    }

    public void updateUserInSystem(String oldUsername, User user) {
        int idx = IMDB.getInstance().findUserIndex(oldUsername);
        User oldUser = IMDB.getInstance().getAccounts().get(idx);
        oldUser.setUsername(user.getUsername());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setCountry(user.getCountry());
        oldUser.setGender(user.getGender());
        oldUser.setBirthDate(user.getBirthDate());
        oldUser.setAge(user.getAge());
        oldUser.setName(user.getName());
        JsonParser.writeAccountsJson(IMDB.getInstance().getAccounts());
    }

    @Override
    public void addProductionSystem(Production p) {
        IMDB.getInstance().getProductions().add(p);
        JsonParser.writeProductionsJson();
    }

    @Override
    public void addActorSystem(Actor a) {
        IMDB.getInstance().getActors().add(a);
        JsonParser.writeActorsJson();
    }

    @Override
    public void removeProductionSystem(String name) {
        IMDB.getInstance().getProductions().remove(IMDB.getInstance().findProduction(name));
        JsonParser.writeProductionsJson();
    }

    @Override
    public void removeActorSystem(String name) {
        IMDB.getInstance().getActors().remove(IMDB.getInstance().findActor(name));
        JsonParser.writeActorsJson();
    }

    @Override
    public void updateProduction(Production p) {
        IMDB.getInstance().getProductions().set(IMDB.getInstance().getProductions().indexOf(p), p);
        JsonParser.writeProductionsJson();
    }

    @Override
    public void updateActor(Actor a) {
        IMDB.getInstance().getActors().set(IMDB.getInstance().getActors().indexOf(a), a);
        JsonParser.writeActorsJson();
    }
}

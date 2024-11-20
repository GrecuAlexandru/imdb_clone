package org.example;

import java.util.List;
import java.util.SortedSet;

public abstract class Staff extends User implements StaffInterface{
    public SortedSet<Comparable<Object>> contributions;
    private List<Request> onlyMyRequests;
    private SortedSet<String> addedActorsOrProductions;
    private List<String> favoriteProductions;
    private List<String> favoriteActors;

    public Staff(Information info,
                 AccountType accountType,
                 String username,
                 Integer experience,
                 List<String> notifications,
                 SortedSet<Comparable<Object>> favorites) {
        super(info, accountType, username, experience, notifications, favorites);
    }

    public void addActorOrProduction(String name) {
        addedActorsOrProductions.add(name);
    }

    public boolean removeActorOrProduction(String name) {
        return addedActorsOrProductions.remove(name);
    }

    public void updateActorsOrProducts(String name, String newName) {
        if (addedActorsOrProductions.contains(name)) {
            addedActorsOrProductions.remove(name);
            addedActorsOrProductions.add(newName);
        }
    }

    // TODO: Rezolvarea cererilor primite de la utilizatori
}

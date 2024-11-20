package org.example;

import org.example.utils.JsonParser;

import java.util.ArrayList;
import java.util.List;

// might need to make static
public class RequestsHolder {
    // toate cererile pe care TOATA ECHIPA DE ADMINI
    // trebuie sa le rezolve
    public static List<Request> adminRequests;

    public static void loadAdminRequests() {
        adminRequests = new ArrayList<>();
        List<Request> requests = IMDB.getInstance().getRequests();
        for (Request request : requests) {
            if (request.getRequestType() == RequestTypes.DELETE_ACCOUNT || request.getRequestType() == RequestTypes.OTHERS) {
                adminRequests.add(request);
            }
        }
    }

    public static void addAdminRequest(Request request) {
        adminRequests.add(request);
        IMDB.getInstance().addRequest(request);
        JsonParser.writeRequestsJson();
    }

    public static void removeAdminRequest(Request request) {
        adminRequests.remove(request);
        IMDB.getInstance().removeRequest(request);
        JsonParser.writeRequestsJson();
    }

    public static List<Request> getAdminRequestsList() {
        return adminRequests;
    }
}

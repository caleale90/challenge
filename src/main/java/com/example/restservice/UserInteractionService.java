package com.example.restservice;

import model.InteractionType;
import model.UserInteraction;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserInteractionService {

    DatabaseController databaseController;

    public UserInteractionService() {
        this.databaseController = new DatabaseController();
    }

    public List<UserInteraction> getUserInteractions(String username, String type) throws SQLException {
        if (type == null || type.isEmpty()) {
            return databaseController.historyByUser(username);
        }

        switch (type.toLowerCase()) {
            case "ratings":
                return databaseController.historyByUsernameAndType(username, InteractionType.RATING);
            case "views":
                return databaseController.historyByUsernameAndType(username, InteractionType.VIEW);
            default:
                throw new IllegalArgumentException("Invalid interaction type: " + type);
        }
    }
}

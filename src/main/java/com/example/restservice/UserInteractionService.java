package com.example.restservice;

import model.InteractionType;
import model.UserInteraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInteractionService {

    @Autowired
    RatingRepository ratingRepository;

    public List<UserInteraction> getUserInteractions(String username, String type) {
        if (type == null || type.isEmpty()) {
            return ratingRepository.historyByUser(username);
        }
        switch (type.toLowerCase()) {
            case "ratings":
                return ratingRepository.historyByUsernameAndType(username, InteractionType.RATING.toString());
            case "views":
                return  ratingRepository.historyByUsernameAndType(username, InteractionType.VIEW.toString());
            default:
                throw new IllegalArgumentException("Invalid interaction type: " + type);
        }
    }
}

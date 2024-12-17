package com.example.restservice;

import org.springframework.stereotype.Service;
import ratingconverter.RatingCalculator;

import java.sql.SQLException;
import java.util.List;

@Service
public class ViewEventController extends EventController {


    public ViewEventController(MovieRepository movieRepository, UserRepository userRepository, RatingRepository ratingRepository) throws SQLException {
        super(movieRepository, userRepository, ratingRepository);
    }

    public void saveView(String username, String movieTitle, Integer viewPercentage) {
        Long movieId = movieRepository.findIdByTitle(movieTitle).orElseThrow(() -> new RuntimeException("Movie not found"));
        Long userId = userRepository.findUserIdByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        int rating = RatingCalculator.calculateRating(viewPercentage);

        if (ratingExists(userId, movieId)) {
            updateExistingRating(viewPercentage, userId, movieId, rating);
        } else {
            ratingRepository.insertRatingWithPercentage(userId, movieId, rating, viewPercentage);
        }
    }

    void updateExistingRating(Integer viewPercentage, Long userId, Long movieId, Integer rating) {
        if (isImplicitRating(userId, movieId)) {
            ratingRepository.updateRatingAndImplicitRating(rating, true, userId, movieId);
        }
        ratingRepository.updateViewPercentage(userId, movieId, viewPercentage);
    }

    protected boolean isImplicitRating(Long userId, Long movieId) {
        return ratingRepository.findRatingsByUserAndMovie(userId, movieId).stream()
                .findFirst()
                .map(rating -> rating.getImplicitRating() == null || rating.getImplicitRating())
                .orElse(false);
    }

}

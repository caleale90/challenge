package com.example.restservice;

import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ViewEventController extends EventController {


    public ViewEventController(MovieRepository movieRepository, UserRepository userRepository, RatingRepository ratingRepository) throws SQLException {
        super(movieRepository, userRepository, ratingRepository);
    }

    public void saveView(String username, String movieTitle, Integer percentage) {
        Long movieId = movieRepository.findIdByTitle(movieTitle).orElseThrow(() -> new RuntimeException("Movie not found"));
        Long userId = userRepository.findUserIdByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        int rating = percentage * 5 / 100;

        if (ratingExists(userId, movieId)) {
            updateExistingRating(percentage, userId, movieId, rating);
        } else {
            ratingRepository.insertRatingWithPercentage(userId, movieId, rating, percentage);
        }
    }

    void updateExistingRating(Integer percentage, Long userId, Long movieId, Integer rating) {
        if (isImplicitRating(userId, movieId)) {
            ratingRepository.updateRatingAndImplicitRating(rating, true, userId, movieId);
        } else
            ratingRepository.updateViewPercentage(userId, movieId, percentage);
    }

    protected boolean isImplicitRating(Long userId, Long movieId) {
        List<Rating> ratingsByUserAndMovie = ratingRepository.findRatingsByUserAndMovie(userId, movieId);
        return ratingsByUserAndMovie.get(0).getImplicitRating();
    }

}

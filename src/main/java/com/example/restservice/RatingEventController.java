package com.example.restservice;

import org.springframework.stereotype.Service;

@Service
public class RatingEventController extends EventController {

    public RatingEventController(MovieRepository movieRepository, UserRepository userRepository, RatingRepository ratingRepository) {
        super(movieRepository, userRepository, ratingRepository);
    }

    public void saveRating(String username, String movieTitle, Integer rating) {
        Long movieId = movieRepository.findIdByTitle(movieTitle).orElseThrow(() -> new RuntimeException("Movie not found"));;
        Long userId = userRepository.findUserIdByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));;

        if (ratingExists(userId, movieId)) {
            ratingRepository.updateRatingAndImplicitRating(rating,false, userId, movieId);
        } else {
            ratingRepository.insertRating(userId, movieId, rating);
        }
    }

}

package com.example.restservice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventController {

    final MovieRepository movieRepository;
    final UserRepository userRepository;
    final RatingRepository ratingRepository;

    public EventController(MovieRepository movieRepository, UserRepository userRepository, RatingRepository ratingRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    boolean ratingExists(Long userId, Long movieId) {
        List<Rating> ratingsByUserAndMovie = ratingRepository.findRatingsByUserAndMovie(userId, movieId);
        return !ratingsByUserAndMovie.isEmpty();
    }
}

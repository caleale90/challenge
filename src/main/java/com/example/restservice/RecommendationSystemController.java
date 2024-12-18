package com.example.restservice;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import requests.RatingRequest;
import requests.ViewRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@RestController
public class RecommendationSystemController {

    @Autowired
    UserInteractionService userInteractionService;

    @Autowired
    RecommendationSystemService recommendationSystemService;

    @Autowired
    RatingEventController ratingStorageService;

    @Autowired
    ViewEventController viewStorageService;

    @Autowired
    RatingRepository ratingRepository;

    @PostMapping("/rating")
    public ResponseEntity<String> rating(@RequestBody RatingRequest request) throws SQLException {
        if (request.rating < 0 || request.rating > 5)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid rating" + request.rating);
        ratingStorageService.saveRating(request.user, request.movie, request.rating);
        return ResponseEntity.status(HttpStatus.OK).body("User " + request.user + " assigned to " + request.movie + " a rating of " + request.rating);
    }

    @PostMapping("/view")
    public ResponseEntity<String> view(@RequestBody ViewRequest request) {
        viewStorageService.saveView(request.user, request.movie, request.viewPercentage);
        return ResponseEntity.status(HttpStatus.OK).body("User " + request.user + " viewed " + request.movie + " until " + request.viewPercentage);
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> getMovies(@RequestParam(required = false) String genre,
                                                    @RequestParam(required = false) Integer minRating,
                                                    @RequestParam(required = false) Integer maxRating) {

        List<String> movies = ratingRepository.findMovies(genre, minRating, maxRating);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<UserInteraction>> getUserInteractions(
            @PathVariable String username,
            @RequestParam(value = "type", required = false) String type) {

        return ResponseEntity.status(HttpStatus.OK).body(userInteractionService.getUserInteractions(username, type));
    }

    @GetMapping("/recommend")
    public ResponseEntity<Set<String>> getRecommendationFor(@RequestParam String username) {
        return ResponseEntity.status(HttpStatus.OK).body(recommendationSystemService.recommendMovies(username));
    }
}
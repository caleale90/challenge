package com.example.restservice;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import requests.RatingRequest;
import requests.ViewRequest;
import validation.MovieValidator;
import validation.PercentageValidator;
import validation.RatingValidator;
import validation.UsernameValidator;

import java.sql.SQLException;
import java.util.List;

@RestController
public class RecommendationSystemController {

    @Autowired
    UserInteractionService userInteractionService;

    @Autowired
    DatabaseController databaseController;

    @Autowired
    RatingEventController ratingStorageService;

    @Autowired
    ViewEventController viewStorageService;

    @PostMapping("/rating")
    public ResponseEntity<String> rating(@RequestBody RatingRequest request) throws SQLException {
        boolean validUser = new UsernameValidator(request.user).isValid();
        boolean validMovie = new MovieValidator(request.movie).isValid();
        boolean validRating = new RatingValidator(request.rating).isValid();

        if (validRating && validUser && validMovie) {
            User user = new User(request.user);
            Movie movie = new Movie(request.movie);
            Rating rating = new Rating(request.rating);
            ratingStorageService.saveRating(user, movie, rating);
            return ResponseEntity.status(HttpStatus.OK).body("User " + request.user + " assigned to " + request.movie + " a rating of " + request.rating);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Movie or user do not exists or rating is not valid");
        }
    }

    @PostMapping("/view")
    public ResponseEntity<String> view(@RequestBody ViewRequest request) throws SQLException {
        boolean validUser = new UsernameValidator(request.user).isValid();
        boolean validMovie = new MovieValidator(request.movie).isValid();
        boolean validPercentage = new PercentageValidator(request.percentage).isValid();

        if (validPercentage && validUser && validMovie) {
            User user = new User(request.user);
            Movie movie = new Movie(request.movie);
            Percentage percentage = new Percentage(request.percentage);
            viewStorageService.saveView(user, movie, percentage);
            return ResponseEntity.status(HttpStatus.OK).body("User " + request.user + " viewed " + request.movie + " until " + request.percentage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Movie or user do not exists or percentage is not valid");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> getMovies(@RequestParam(required = false) String genre,
                                                 @RequestParam(required = false) Integer minRating,
                                                 @RequestParam(required = false) Integer maxRating) throws SQLException {
        return ResponseEntity.status(HttpStatus.OK).body(databaseController.findMovies(genre, minRating, maxRating));
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<UserInteraction>> getUserInteractions(
            @PathVariable String username,
            @RequestParam(value = "type", required = false) String type) throws SQLException {

        return ResponseEntity.status(HttpStatus.OK).body(userInteractionService.getUserInteractions(username, type));
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<Movie>> getRecommendationFor(@RequestParam String username) throws SQLException {
        return ResponseEntity.status(HttpStatus.OK).body(databaseController.recommendByUsername(username));
    }
}
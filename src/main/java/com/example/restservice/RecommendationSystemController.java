package com.example.restservice;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
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
    RecommendationSystem recommendationSystem;

    @Autowired
    RatingEventController ratingStorageService;

    @Autowired
    ViewEventController viewStorageService;

    @Autowired
    RatingRepository ratingRepository;

    @PostMapping("/rating")
    public ResponseEntity<String> rating(@RequestBody RatingRequest request) throws SQLException {
        ratingStorageService.saveRating(request.user, request.movie, request.rating);
        return ResponseEntity.status(HttpStatus.OK).body("User " + request.user + " assigned to " + request.movie + " a rating of " + request.rating);
    }

    @PostMapping("/view")
    public ResponseEntity<String> view(@RequestBody ViewRequest request) {
        viewStorageService.saveView(request.user, request.movie, request.viewPercentage);
        return ResponseEntity.status(HttpStatus.OK).body("User " + request.user + " viewed " + request.movie + " until " + request.viewPercentage);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Object[]>> getMovies(@RequestParam(required = false) String genre,
                                                    @RequestParam(required = false) Integer minRating,
                                                    @RequestParam(required = false) Integer maxRating) {

        List<Object[]> movies = ratingRepository.findMovies(genre, minRating, maxRating);
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
        return ResponseEntity.status(HttpStatus.OK).body(recommendationSystem.recommendMovies(username));
    }

    @Bean
    public CommandLineRunner demo(MovieRepository repository, UserRepository userRepository, RatingRepository ratingRepository) {
        return (args) -> {
            Movie toyStory = new Movie("Toy Story", "Adventure|Animation|Children|Comedy|Fantasy");
            Movie grumpierOldMen = new Movie("Grumpier Old Men", "Comedy|Romance");
            Movie dieHard = new Movie("Die Hard", "Action|Thriller");
            Movie sw = new Movie("Star Wars: Return of the Jedi", "Action|Adventure|Fantasy|Sci-Fi");
            Movie tlk = new Movie("The Lion King", "Adventure|Animation|Children|Drama|Musical");
            Movie pu = new Movie("Pulp Fiction", "Crime|Drama|Thriller");
            Movie forrestGump = new Movie("Forrest Gump", "Comedy|Drama|Romance");
            Movie th = new Movie("The Matrix", "Action|Sci-Fi");
            Movie go = new Movie("Goodfellas", "Biography|Crime|Drama");
            Movie jurassicPark = new Movie("Jurassic Park", "Adventure|Sci-Fi|Thriller");
            repository.save(toyStory);
            repository.save(grumpierOldMen);
            repository.save(dieHard);
            repository.save(sw);
            repository.save(tlk);
            repository.save(pu);
            repository.save(forrestGump);
            repository.save(th);
            repository.save(go);
            repository.save(jurassicPark);

            User alice = new User("Alice");
            User bob = new User("Bob");
            User charlie = new User("Charlie");
            userRepository.save(alice);
            userRepository.save(bob);
            userRepository.save(charlie);

            ratingRepository.save(new Rating(alice, toyStory, 4, 85, null));
            ratingRepository.save(new Rating(alice, grumpierOldMen, 5, null, null));
            ratingRepository.save(new Rating(bob, toyStory, null, 90, null));
            ratingRepository.save(new Rating(bob, dieHard, 3, null, null));
            ratingRepository.save(new Rating(charlie, sw, null, 70, null));
            ratingRepository.save(new Rating(charlie, grumpierOldMen, 2, null, null));

        };
    }


}
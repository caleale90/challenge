package com.example.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RatingTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    private User user;
    private Movie movie;
    private Rating rating;

    @BeforeEach
    public void setUp() {
        user = new User("John");
        movie = new Movie("Inception", "Sci-Fi|Drama");

        userRepository.save(user);
        movieRepository.save(movie);

        rating = new Rating(user, movie, 5, 100, true);
    }

    @Test
    public void testRatingEntityCreation() {
        ratingRepository.save(rating);

        assertNotNull(rating.getId(), "The rating ID should be generated.");
        assertEquals(user, rating.getUser(), "The user should match the expected.");
        assertEquals(movie, rating.getMovie(), "The movie should match the expected.");
        assertEquals(5, rating.getRating(), "The rating value should be 5.");
        assertEquals(100, rating.getViewPercentage(), "The view percentage should be 100.");
        assertTrue(rating.getImplicitRating(), "The implicit rating should be true.");
    }

}

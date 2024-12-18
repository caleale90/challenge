package com.example.restservice;

import model.UserInteraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    private User user;
    private Movie movie;
    private Rating rating;

    @BeforeEach
    public void setUp() {
        user = new User("JohnDoe");
        userRepository.save(user);

        movie = new Movie("Inception", "Sci-Fi|Drama");
        movieRepository.save(movie);

        rating = new Rating(user, movie, 5, 80, false);
        ratingRepository.save(rating);
    }

    @Test
    public void testFindRatingsByUserAndMovie() {
        List<Rating> ratings = ratingRepository.findRatingsByUserAndMovie(user.getId(), movie.getId());
        assertEquals(1, ratings.size(), "There should be one rating found.");
        assertEquals(rating.getRating(), ratings.get(0).getRating(), "The rating value should match.");
    }

    @Test
    public void testInsertRating() {
        ratingRepository.insertRating(user.getId(), movie.getId(), 3);

        List<Rating> ratings = ratingRepository.findRatingsByUserAndMovie(user.getId(), movie.getId());
        assertEquals(2, ratings.size(), "There should be two ratings found after insertion.");
    }

    @Test
    public void testFindMovies() {
        List<String> movies = ratingRepository.findMovies("Sci-Fi", null, null);

        assertEquals(1, movies.size(), "There should be one movie matching the genre.");
        assertEquals("Inception", movies.get(0), "The movie title should match.");
    }

    @Test
    public void testHistoryByUser() {
        List<UserInteraction> interactions = ratingRepository.historyByUser("JohnDoe");

        assertEquals(1, interactions.size(), "There should be one interaction.");
        assertEquals("Inception", interactions.get(0).getMovieTitle(), "The movie title should match.");
    }

    @Test
    public void testHistoryByUsernameAndType() {
        List<UserInteraction> interactions = ratingRepository.historyByUsernameAndType("JohnDoe", "VIEW");

        assertEquals(1, interactions.size(), "There should be one interaction.");
        assertEquals("Inception", interactions.get(0).getMovieTitle(), "The movie title should match.");
    }

    @Test
    public void testFindFavouriteGenresByUsername() {
        List<String> genres = ratingRepository.findFavouriteGenresByUsername("JohnDoe");

        assertEquals(1, genres.size(), "There should be one favorite genre.");
        assertTrue(genres.get(0).contains("Sci-Fi"), "The genre should match.");
    }

    @Test
    public void testFindRatedFilms() {
        Set<String> ratedMovies = ratingRepository.findRatedFilms("JohnDoe");

        assertEquals(1, ratedMovies.size(), "There should be one rated film.");
        assertTrue(ratedMovies.contains("Inception"), "The rated film should match.");
    }

    @Test
    public void testIsImplicitRating() {
        boolean isImplicit = ratingRepository.isImplicitRating(user.getId(), movie.getId());

        assertFalse(isImplicit, "The implicit rating should be false.");
    }
}

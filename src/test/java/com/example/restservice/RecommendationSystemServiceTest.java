package com.example.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RecommendationSystemServiceTest {

    @Mock
    RatingRepository ratingRepository;

    @InjectMocks
    RecommendationSystemService recommendationSystemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRecommendMovies_withNoFavouriteGenres() {
        String username = "john_doe";
        List<String> favouriteGenresByUsername = new ArrayList<>();

        when(ratingRepository.findFavouriteGenresByUsername(username)).thenReturn(favouriteGenresByUsername);

        Set<String> recommendedMovies = recommendationSystemService.recommendMovies(username);

        assertTrue(recommendedMovies.isEmpty());
    }

    @Test
    void testRecommendMovies_withFavouriteGenres() {
        String username = "john_doe";
        List<String> favouriteGenresByUsername = Arrays.asList("Action", "Comedy");
        Set<String> otherMoviesOfSameGenres = new HashSet<>(Arrays.asList("Movie1", "Movie2", "Movie3"));
        Set<String> ratedFilms = new HashSet<>(Arrays.asList("Movie1"));

        when(ratingRepository.findFavouriteGenresByUsername(username)).thenReturn(favouriteGenresByUsername);
        when(ratingRepository.findRecommendedMovies("%(Action|Comedy)%")).thenReturn(otherMoviesOfSameGenres);
        when(ratingRepository.findRatedFilms(username)).thenReturn(ratedFilms);

        Set<String> recommendedMovies = recommendationSystemService.recommendMovies(username);

        assertEquals(2, recommendedMovies.size());
        assertTrue(recommendedMovies.contains("Movie2"));
        assertTrue(recommendedMovies.contains("Movie3"));
        assertFalse(recommendedMovies.contains("Movie1"));
    }


    @Test
    void testExtractGenres(){
        RecommendationSystemService recommendationSystemService = new RecommendationSystemService();
        ArrayList<String> genres = new ArrayList<>();
        genres.add("1|2");
        genres.add("3|2");
        Set<String> result = recommendationSystemService.extractGenres(genres);
        assertEquals(3, result.size());
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
        assertTrue(result.contains("3"));
    }

}
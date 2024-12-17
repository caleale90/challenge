package com.example.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RatingRepository ratingRepository;

    private EventController eventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventController = new EventController(movieRepository, userRepository, ratingRepository);
    }

    @Test
    void ratingExistsWhenRatingsExist() {
        Long userId = 1L;
        Long movieId = 101L;
        List<Rating> mockRatings = List.of(new Rating());
        when(ratingRepository.findRatingsByUserAndMovie(userId, movieId)).thenReturn(mockRatings);

        assertTrue(eventController.ratingExists(userId, movieId));
        verify(ratingRepository, times(1)).findRatingsByUserAndMovie(userId, movieId);
    }

    @Test
    void ratingExistsWhenNoRatingsExist() {
        Long userId = 2L;
        Long movieId = 202L;
        when(ratingRepository.findRatingsByUserAndMovie(userId, movieId)).thenReturn(Collections.emptyList());

        assertFalse(eventController.ratingExists(userId, movieId));
        verify(ratingRepository, times(1)).findRatingsByUserAndMovie(userId, movieId);
    }
}

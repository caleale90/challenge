package com.example.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViewEventControllerTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private ViewEventController viewEventController;

    private String validUsername = "john_doe";
    private String validMovieTitle = "Inception";
    private Integer viewPercentage = 80;
    private Integer rating = 4;
    private Long validUserId = 1L;
    private Long validMovieId = 1L;

    @BeforeEach
    void setUp() {
        viewEventController = spy(viewEventController);
    }

    @Test
    void testSaveViewWhenRatingDoesNotExist() {
        when(movieRepository.findIdByTitle(validMovieTitle)).thenReturn(Optional.of(validMovieId));
        when(userRepository.findUserIdByUsername(validUsername)).thenReturn(Optional.of(validUserId));

        when(viewEventController.ratingExists(validUserId, validMovieId)).thenReturn(false);

        viewEventController.saveView(validUsername, validMovieTitle, viewPercentage);

        verify(ratingRepository, times(1)).insertRatingWithPercentage(validUserId, validMovieId, 4, viewPercentage);
        verify(ratingRepository, never()).updateRatingAndImplicitRating(anyInt(), anyBoolean(), anyLong(), anyLong());
        verify(ratingRepository, never()).updateViewPercentage(anyLong(), anyLong(), anyInt());
    }

    @Test
    void testSaveViewWhenRatingExists() {
        when(movieRepository.findIdByTitle(validMovieTitle)).thenReturn(Optional.of(validMovieId));
        when(userRepository.findUserIdByUsername(validUsername)).thenReturn(Optional.of(validUserId));

        when(viewEventController.ratingExists(validUserId, validMovieId)).thenReturn(true);
        doNothing().when(viewEventController).updateExistingRating(viewPercentage, validUserId, validMovieId, rating);

        viewEventController.saveView(validUsername, validMovieTitle, viewPercentage);

        verify(viewEventController, times(1)).updateExistingRating(viewPercentage, validUserId, validMovieId, rating);
        verify(ratingRepository, never()).insertRatingWithPercentage(anyLong(), anyLong(), anyInt(), anyInt());
    }


    @Test
    void testSaveViewWhenMovieNotFound() {
        when(movieRepository.findIdByTitle(validMovieTitle)).thenReturn(Optional.empty());

        try {
            viewEventController.saveView(validUsername, validMovieTitle, viewPercentage);
        } catch (RuntimeException e) {
            assertEquals("Movie not found", e.getMessage());
        }

        verify(viewEventController, never()).ratingExists(anyLong(), anyLong());
        verify(ratingRepository, never()).insertRatingWithPercentage(anyLong(), anyLong(), anyInt(), anyInt());
    }

    @Test
    void testSaveViewWhenUserNotFound() {
        when(movieRepository.findIdByTitle(validMovieTitle)).thenReturn(Optional.of(validMovieId));
        when(userRepository.findUserIdByUsername(validUsername)).thenReturn(Optional.empty());

        try {
            viewEventController.saveView(validUsername, validMovieTitle, viewPercentage);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }

        verify(ratingRepository, never()).insertRatingWithPercentage(anyLong(), anyLong(), anyInt(), anyInt());
        verify(ratingRepository, never()).updateRatingAndImplicitRating(anyInt(), anyBoolean(), anyLong(), anyLong());
        verify(ratingRepository, never()).updateViewPercentage(anyLong(), anyLong(), anyInt());
    }

    @Test
    void testUpdateExistingRatingWhenImplicit() {
        doReturn(true).when(viewEventController).isImplicitRating(validUserId, validMovieId);

        doNothing().when(ratingRepository).updateRatingAndImplicitRating(rating, true, validUserId, validMovieId);

        viewEventController.updateExistingRating(viewPercentage, validUserId, validMovieId, rating);

        verify(viewEventController, times(1)).isImplicitRating(validUserId, validMovieId);
        verify(ratingRepository, times(1)).updateRatingAndImplicitRating(4, true, validUserId, validMovieId);
        verify(ratingRepository, times(1)).updateViewPercentage(validUserId, validMovieId, viewPercentage);

    }

    @Test
    void testUpdateExistingRatingWhenIsNotImplicit() {
        doReturn(false).when(viewEventController).isImplicitRating(validUserId, validMovieId);

        viewEventController.updateExistingRating(viewPercentage, validUserId, validMovieId, rating);

        verify(viewEventController, times(1)).isImplicitRating(validUserId, validMovieId);
        verify(ratingRepository, never()).updateRatingAndImplicitRating(anyInt(), anyBoolean(), anyLong(), anyLong());
        verify(ratingRepository, times(1)).updateViewPercentage(validUserId, validMovieId, viewPercentage);
    }

    @Test
    void testIsImplicitRatingWhenNull() {
        ArrayList<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating(new User("username"), new Movie("movie", "genre"), 3, 3, null));

        when(ratingRepository.findRatingsByUserAndMovie(validUserId, validMovieId)).thenReturn(ratings);

        assertFalse(viewEventController.isImplicitRating(validUserId,validMovieId));
    }

    @Test
    void testIsImplicitRating() {
        ArrayList<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating(new User("username"), new Movie("movie", "genre"), 3, 3, true));

        when(ratingRepository.findRatingsByUserAndMovie(validUserId, validMovieId)).thenReturn(ratings);

        assertTrue(viewEventController.isImplicitRating(validUserId,validMovieId));
    }
}

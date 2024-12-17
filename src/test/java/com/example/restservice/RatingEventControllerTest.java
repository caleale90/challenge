package com.example.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RatingEventControllerTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingEventController ratingEventController;

    private final String validUsername = "john_doe";
    private final String validMovieTitle = "Inception";
    private final Integer validRating = 5;
    private final Long validMovieId = 1L;
    private final Long validUserId = 1L;

    @BeforeEach
    public void setUp() {
        ratingEventController = spy(ratingEventController);
    }

    @Test
    public void testSaveRatingWhenRatingDoesNotExist() {
        when(movieRepository.findIdByTitle(validMovieTitle)).thenReturn(java.util.Optional.of(validMovieId));
        when(userRepository.findUserIdByUsername(validUsername)).thenReturn(Optional.of(validUserId));

        when(ratingEventController.ratingExists(validUserId, validMovieId)).thenReturn(false);

        ratingEventController.saveRating(validUsername, validMovieTitle, validRating);

        verify(ratingRepository, times(1)).insertRating(validUserId, validMovieId, validRating);
        verify(ratingRepository, never()).updateRatingAndImplicitRating(anyInt(), anyBoolean(), anyLong(), anyLong());
    }

    @Test
    public void testSaveRatingWhenRatingExists() {
        when(movieRepository.findIdByTitle(validMovieTitle)).thenReturn(Optional.of(validMovieId));
        when(userRepository.findUserIdByUsername(validUsername)).thenReturn(Optional.of(validUserId));

        when(ratingEventController.ratingExists(validUserId, validMovieId)).thenReturn(true);

        ratingEventController.saveRating(validUsername, validMovieTitle, validRating);

        verify(ratingRepository, never()).insertRating(anyLong(), anyLong(), anyInt());
        verify(ratingRepository, times(1)).updateRatingAndImplicitRating(validRating, false, validUserId, validMovieId);
    }

    @Test
    public void testSaveRatingWhenMovieDoesNotExist() {
        when(movieRepository.findIdByTitle(validMovieTitle)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                ratingEventController.saveRating(validUsername, validMovieTitle, validRating)
        );
        assertEquals("Movie not found", exception.getMessage());
    }

    @Test
    public void testSaveRatingWhenUserDoesNotExist() {
        when(movieRepository.findIdByTitle(validMovieTitle)).thenReturn(java.util.Optional.of(validMovieId));
        when(userRepository.findUserIdByUsername(validUsername)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                ratingEventController.saveRating(validUsername, validMovieTitle, validRating)
        );
        assertEquals("User not found", exception.getMessage());
    }
}
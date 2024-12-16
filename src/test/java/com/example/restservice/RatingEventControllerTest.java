package com.example.restservice;

import model.Movie;
import model.Rating;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

class RatingEventControllerTest {

    @Mock
    private User user;

    @Mock
    private Movie movie;

    @Mock
    private Rating rating;

    @InjectMocks
    private RatingEventController ratingEventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ratingEventController = spy(ratingEventController);
    }

    @Test
    void testSaveRatingWhenRatingExists() throws SQLException {
        when(movie.getTitle()).thenReturn("title");
        when(user.getUsername()).thenReturn("username");
        when(ratingEventController.getMovieId("title")).thenReturn(1);
        when(ratingEventController.getUserId("username")).thenReturn(1);
        when(rating.getRatingValue()).thenReturn(3);
        when(ratingEventController.ratingExists(user, movie)).thenReturn(true);

        doNothing().when(ratingEventController).updateRating(1, 1, 3, false);

        ratingEventController.saveRating(user, movie, rating);

        verify(ratingEventController, times(1)).updateRating(1, 1, 3, false);
        verify(ratingEventController, never()).storeRating(anyInt(), anyInt(), anyInt());

    }

    @Test
    void testSaveRatingWhenRatingDoesNotExist() throws SQLException {
        when(movie.getTitle()).thenReturn("title");
        when(user.getUsername()).thenReturn("username");
        when(ratingEventController.getMovieId("title")).thenReturn(1);
        when(ratingEventController.getUserId("username")).thenReturn(1);
        when(rating.getRatingValue()).thenReturn(3);

        doNothing().when(ratingEventController).storeRating(1, 1, 3);

        ratingEventController.saveRating(user, movie, rating);

        verify(ratingEventController, never()).updateRating(anyInt(), anyInt(), anyInt(), anyBoolean());
        verify(ratingEventController, times(1)).storeRating(1, 1, 3);
    }

    @Test
    void testUpdateRating() throws SQLException {
        DatabaseConnection mockDbConnection = mock(DatabaseConnection.class);
        ReflectionTestUtils.setField(ratingEventController, "databaseConnection", mockDbConnection);
        String query = "UPDATE public.ratings SET rating = ?, implicit_rating = ? WHERE user_id = ? AND movie_id = ?";

        doNothing().when(mockDbConnection).updateRatingQuery(query, 1, true, 3, 1);

        ratingEventController.updateRating(1, 1, 3, true);
        verify(mockDbConnection, times(1)).updateRatingQuery(query, 3, true, 1, 1);
    }

    @Test
    void testStoreRating() throws SQLException {
        DatabaseConnection mockDbConnection = mock(DatabaseConnection.class);
        ReflectionTestUtils.setField(ratingEventController, "databaseConnection", mockDbConnection);
        String query = "INSERT INTO public.ratings (user_id, movie_id, rating, implicit_rating) VALUES (?, ?, ?, false)";

        doNothing().when(mockDbConnection).insertQuery(query, 1, 1, 3);

        ratingEventController.storeRating(1, 1, 3);
        verify(mockDbConnection, times(1)).insertQuery(query, 1, 1, 3);
    }
}
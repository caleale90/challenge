package com.example.restservice;

import model.Movie;
import model.Percentage;
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

public class ViewEventControllerTest {

    @Mock
    private User user;

    @Mock
    private Movie movie;

    @Mock
    private Percentage percentage;

    @InjectMocks
    private ViewEventController viewEventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        viewEventController = spy(viewEventController);
    }

    @Test
    void testSaveViewWhenRatingExists() throws SQLException {
        when(movie.getTitle()).thenReturn("Movie Title");
        when(user.getUsername()).thenReturn("user123");
        when(percentage.getValue()).thenReturn(50);

        when(viewEventController.getMovieId("Movie Title")).thenReturn(1);
        when(viewEventController.getUserId("user123")).thenReturn(1);
        when(viewEventController.ratingExists(user, movie)).thenReturn(true);

        doNothing().when(viewEventController).updatePercentage(1, 1, 50);

        viewEventController.saveView(user, movie, percentage);

        verify(viewEventController).updatePercentage(1, 1, 50);
        verify(viewEventController, never()).storeRatingWithPercentage(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void testSaveViewWhenRatingDoesNotExist() throws SQLException {
        when(movie.getTitle()).thenReturn("Movie Title");
        when(user.getUsername()).thenReturn("user123");
        when(percentage.toRating()).thenReturn(new Rating(3));
        when(percentage.getValue()).thenReturn(50);

        when(viewEventController.getMovieId("Movie Title")).thenReturn(1);
        when(viewEventController.getUserId("user123")).thenReturn(1);
        when(viewEventController.ratingExists(user, movie)).thenReturn(false);

        doNothing().when(viewEventController).storeRatingWithPercentage(1, 1, 3, 50);

        viewEventController.saveView(user, movie, percentage);

        verify(viewEventController, never()).updatePercentage(anyInt(), anyInt(), anyInt());
        verify(viewEventController).storeRatingWithPercentage(1, 1, 3, 50);
    }

    @Test
    void testUpdatePercentage() throws SQLException {
        DatabaseConnection mockDbConnection = mock(DatabaseConnection.class);
        ReflectionTestUtils.setField(viewEventController, "databaseConnection", mockDbConnection);
        String query = "UPDATE public.ratings SET view_percentage = ? WHERE user_id = ? AND movie_id = ?";
        doNothing().when(mockDbConnection).updateOnlyPercentageQuery(query, 1, 1, 75);

        viewEventController.updatePercentage(1, 1, 75);

        verify(mockDbConnection, times(1)).updateOnlyPercentageQuery(query, 1, 1, 75);
    }

    @Test
    void testStoreRatingWithPercentage() throws SQLException {
        DatabaseConnection mockDbConnection = mock(DatabaseConnection.class);
        ReflectionTestUtils.setField(viewEventController, "databaseConnection", mockDbConnection);
        String query = "INSERT INTO public.ratings (user_id, movie_id, rating, view_percentage, implicit_rating) VALUES (?, ?, ?, ?, true)";
        doNothing().when(mockDbConnection).insertQuery(anyString(), anyInt(), anyInt(), anyInt(), anyInt());

        viewEventController.storeRatingWithPercentage(1, 1, 5, 75);

        verify(mockDbConnection, times(1)).insertQuery(query, 1, 1, 5, 75);
    }
}

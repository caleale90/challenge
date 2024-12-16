package com.example.restservice;

import model.Movie;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventControllerTest {

    private EventController eventController;
    private DatabaseConnection mockDbConnection;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        mockDbConnection = mock(DatabaseConnection.class);
        eventController = new EventController();
        ReflectionTestUtils.setField(eventController, "databaseConnection", mockDbConnection);
    }

    @Test
    public void testGetMovieIdFound() throws SQLException {
        String title = "Inception";
        int expectedMovieId = 1;

        mockResultSet = mock(ResultSet.class);
        when(mockDbConnection.executeQuery(anyString(), eq(title))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("movie_id")).thenReturn(expectedMovieId);

        assertEquals(expectedMovieId, eventController.getMovieId(title));
    }

    @Test
    public void testGetMovieIdNotFound() throws SQLException {
        String title = "NonExistentMovie";

        mockResultSet = mock(ResultSet.class);
        when(mockDbConnection.executeQuery(anyString(), eq(title))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertEquals(-1, eventController.getMovieId(title));
    }

    @Test
    public void testGetUserIdFound() throws SQLException {
        String username = "john_doe";
        int expectedUserId = 100;

        mockResultSet = mock(ResultSet.class);
        when(mockDbConnection.executeQuery(anyString(), eq(username))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("user_id")).thenReturn(expectedUserId);

        assertEquals(expectedUserId, eventController.getUserId(username));
    }

    @Test
    public void testGetUserIdNotFound() throws SQLException {
        String username = "non_existent_user";

        mockResultSet = mock(ResultSet.class);
        when(mockDbConnection.executeQuery(anyString(), eq(username))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertEquals(-1, eventController.getUserId(username));
    }

    @Test
    public void testRatingExists() throws SQLException {
        User user = new User("john_doe");
        Movie movie = new Movie("Inception");

        mockResultSet = mock(ResultSet.class);
        when(eventController.getRating(user, movie)).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);  // Simulate that a rating exists

        assertTrue(eventController.ratingExists(user, movie));
    }

    @Test
    public void testRatingNotExists() throws SQLException {
        User user = new User("john_doe");
        Movie movie = new Movie("NonExistentMovie");

        mockResultSet = mock(ResultSet.class);
        when(eventController.getRating(user, movie)).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);  // Simulate that no rating exists

        assertFalse(eventController.ratingExists(user, movie));
    }

}

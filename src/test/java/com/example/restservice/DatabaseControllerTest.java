package com.example.restservice;

import model.InteractionType;
import model.Movie;
import model.UserInteraction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatabaseControllerTest {

    @Mock
    private DatabaseConnection mockDatabaseConnection;

    @Mock
    private ResultSet mockResultSet;

    private DatabaseController databaseController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        databaseController = new DatabaseController();
        mockDatabaseConnection = mock(DatabaseConnection.class);
        ReflectionTestUtils.setField(databaseController, "databaseConnection", mockDatabaseConnection);
    }
    @AfterEach
    public void tearDown() {
        Mockito.reset(mockDatabaseConnection, mockResultSet);
    }

    @Test
    public void testUsernameExistsReturnsTrueWhenUsernameExists() throws SQLException {
        String username = "testUser";
        when(mockDatabaseConnection.executeQuery(anyString(), eq(username))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        assertTrue(databaseController.usernameExists(username));
    }

    @Test
    public void testUsernameExistsReturnsFalseWhenUsernameDoesNotExist() throws SQLException {
        String username = "nonExistentUser";
        when(mockDatabaseConnection.executeQuery(anyString(), eq(username))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertFalse(databaseController.usernameExists(username));
    }

    @Test
    public void testMovieExistsReturnsTrueWhenMovieExists() throws SQLException {
        String movieTitle = "Inception";
        when(mockDatabaseConnection.executeQuery(anyString(), eq(movieTitle))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        assertTrue(databaseController.movieExists(movieTitle));
    }

    @Test
    public void testMovieExistsReturnsFalseWhenMovieDoesNotExist() throws SQLException {
        String movieTitle = "Unknown Movie";
        when(mockDatabaseConnection.executeQuery(anyString(), eq(movieTitle))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertFalse(databaseController.movieExists(movieTitle));
    }

    @Test
    public void testFindMoviesReturnsMoviesWhenValidGenreAndRatingProvided() throws SQLException {
        String genre = "Action";
        Integer minRating = 4;
        Integer maxRating = 5;

        String expectedQuery = "SELECT title, AVG(rating) FROM public.movies JOIN public.ratings ON movies.movie_id = ratings.movie_id WHERE (implicit_rating IS NOT NULL AND implicit_rating <> true) AND genres ILIKE '%' || ? || '%' GROUP BY title HAVING AVG(rating) >= ? AND AVG(rating) <= ?";

        when(mockDatabaseConnection.executeQuery(eq(expectedQuery), eq(genre), eq(minRating), eq(maxRating))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("title")).thenReturn("Die Hard");

        List<Movie> movies = databaseController.findMovies(genre, minRating, maxRating);

        assertEquals(1, movies.size());
        assertEquals("Die Hard", movies.get(0).getTitle());
    }

    @Test
    public void testHistoryByUserReturnsUserInteractions() throws SQLException {
        String username = "testUser";

        when(mockDatabaseConnection.executeQuery(anyString(), eq(username))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("title")).thenReturn("Inception");
        when(mockResultSet.getInt("rating")).thenReturn(5);
        when(mockResultSet.getInt("view_percentage")).thenReturn(80);
        when(mockResultSet.getBoolean("implicit_rating")).thenReturn(false);

        List<UserInteraction> interactions = databaseController.historyByUser(username);

        assertEquals(1, interactions.size());
        assertEquals("Inception", interactions.get(0).getMovie().getTitle());
        assertEquals(5, interactions.get(0).getRating().getRatingValue());
        assertEquals(80, interactions.get(0).getPercentage().getValue());
    }

    @Test
    public void testHistoryByUsernameAndType_ReturnsCorrectInteractions() throws SQLException {
        String username = "testUser";
        InteractionType interactionType = InteractionType.RATING;

        when(mockDatabaseConnection.executeQuery(anyString(), eq(username))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("title")).thenReturn("Inception");
        when(mockResultSet.getInt("rating")).thenReturn(5);
        when(mockResultSet.getInt("view_percentage")).thenReturn(80);
        when(mockResultSet.getBoolean("implicit_rating")).thenReturn(false);

        List<UserInteraction> interactions = databaseController.historyByUsernameAndType(username, interactionType);

        assertEquals(1, interactions.size());
        assertEquals("Inception", interactions.get(0).getMovie().getTitle());
        assertEquals(5, interactions.get(0).getRating().getRatingValue());
    }

    @Test
    public void testGetRecommendedMovies_ReturnsRecommendationsBasedOnGenres() throws SQLException {
        String username = "testUser";

        when(mockDatabaseConnection.executeQuery(anyString(), eq(username))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("genres")).thenReturn("Action|Adventure");

        when(mockDatabaseConnection.executeQuery(anyString(), eq("%(Action|Adventure)%"), eq(username)))
                .thenReturn(mockResultSet);
        when(mockResultSet.getString("title")).thenReturn("Mad Max: Fury Road");

        List<Movie> recommendedMovies = databaseController.recommendByUsername(username);

        assertEquals(1, recommendedMovies.size());
        assertEquals("Mad Max: Fury Road", recommendedMovies.get(0).getTitle());
    }
}

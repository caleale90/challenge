package com.example.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class DatabaseConnectionTest {
    private DatabaseConnection databaseConnection;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        databaseConnection = new DatabaseConnection();
        ReflectionTestUtils.setField(databaseConnection, "connection", mockConnection);
    }

    @Test
    void testGetInstance() throws SQLException {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();

        assertSame(instance1, instance2, "The instance should be the same");
    }

    @Test
    void testExecuteQueryWithArgs() throws SQLException {
        String query = "SELECT * FROM movies WHERE genre = ?";
        String genre = "Action";

        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        ResultSet resultSet = databaseConnection.executeQuery(query, genre);

        verify(mockStatement, times(1)).setString(1, genre);
        verify(mockStatement, times(1)).executeQuery();
        assertNotNull(resultSet, "ResultSet should not be null");
    }

    @Test
    void testInsertQuery() throws SQLException {
        String query = "INSERT INTO ratings (user_id, movie_id, rating) VALUES (?, ?, ?)";
        int userId = 1;
        int movieId = 2;
        int rating = 5;

        databaseConnection.insertQuery(query, userId, movieId, rating);

        verify(mockStatement, times(1)).setInt(1, userId);
        verify(mockStatement, times(1)).setInt(2, movieId);
        verify(mockStatement, times(1)).setInt(3, rating);
        verify(mockStatement, times(1)).execute();
    }

    @Test
    void testUpdateRatingQuery() throws SQLException {
        String query = "UPDATE ratings SET rating = ?, implicit_rating = ? WHERE user_id = ? AND movie_id = ?";
        int rating = 4;
        boolean implicitRating = true;
        int userId = 1;
        int movieId = 2;

        databaseConnection.updateRatingQuery(query, rating, implicitRating, userId, movieId);

        verify(mockStatement, times(1)).setInt(1, rating);
        verify(mockStatement, times(1)).setBoolean(2, implicitRating);
        verify(mockStatement, times(1)).setInt(3, userId);
        verify(mockStatement, times(1)).setInt(4, movieId);
        verify(mockStatement, times(1)).execute();
    }

    @Test
    void testUpdateOnlyPercentageQuery() throws SQLException {
        String query = "UPDATE ratings SET view_percentage = ? WHERE user_id = ? AND movie_id = ?";
        int percentage = 75;
        int userId = 1;
        int movieId = 2;

        databaseConnection.updateOnlyPercentageQuery(query, userId, movieId, percentage);

        verify(mockStatement, times(1)).setInt(1, percentage);
        verify(mockStatement, times(1)).setInt(2, userId);
        verify(mockStatement, times(1)).setInt(3, movieId);
        verify(mockStatement, times(1)).execute();
    }

    @Test
    void testExecuteQueryWithGenreAndRating() throws SQLException {
        String query = "SELECT * FROM movies WHERE genre = ? AND rating BETWEEN ? AND ?";
        String genre = "Action";
        Integer minRating = 4;
        Integer maxRating = 5;

        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        ResultSet resultSet = databaseConnection.executeQuery(query, genre, minRating, maxRating);

        verify(mockStatement, times(1)).setString(1, genre);
        verify(mockStatement, times(1)).setDouble(2, minRating);
        verify(mockStatement, times(1)).setDouble(3, maxRating);
        verify(mockStatement, times(1)).executeQuery();
        assertNotNull(resultSet, "ResultSet should not be null");
    }
}

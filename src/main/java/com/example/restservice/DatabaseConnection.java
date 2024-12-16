package com.example.restservice;

import java.sql.*;


public class DatabaseConnection {
    String jdbcUrl = "jdbc:postgresql://postgres:5432/challenge";
    String username = "postgres";
    String password = "postgres";
    Connection connection;

    private static DatabaseConnection instance;

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(jdbcUrl, username, password);
    }

    public ResultSet executeQuery(String query, String ... args) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setString(i + 1, args[i]);
        }
        return preparedStatement.executeQuery();
    }

    public void insertQuery(String query, int userId, int movieId, int rating) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.setInt(2, movieId);
        stmt.setInt(3, rating);
        stmt.execute();
    }

    public void insertQuery(String query, int userId, int movieId, int rating, int viewPercentage) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.setInt(2, movieId);
        stmt.setInt(3, rating);
        stmt.setInt(4, viewPercentage);
        stmt.execute();
    }

    public void updateRatingQuery(String query, int rating, boolean implicitRating, int userId, int movieId) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, rating);
        stmt.setBoolean(2, implicitRating);
        stmt.setInt(3, userId);
        stmt.setInt(4, movieId);
        stmt.execute();
    }


    public void updateOnlyPercentageQuery(String query, int userId, int movieId, int percentage) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, percentage);
        stmt.setInt(2, userId);
        stmt.setInt(3, movieId);
        stmt.execute();
    }

    public ResultSet executeQuery(String query, String genre, Integer minRating, Integer maxRating) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        int paramIndex = 1;
        if (genre != null && !genre.isEmpty()) {
            stmt.setString(paramIndex++, genre);
        }
        if (minRating != null) {
            stmt.setDouble(paramIndex++, minRating);
        }
        if (maxRating != null) {
            stmt.setDouble(paramIndex, maxRating);
        }
        return stmt.executeQuery();
    }
}

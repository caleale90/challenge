package com.example.restservice;

import model.*;
import model.InteractionType;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class DatabaseController {
    private DatabaseConnection databaseConnection;

    public DatabaseController() {
        try {
            this.databaseConnection = DatabaseConnection.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        String query = "SELECT 1 FROM public.users WHERE username = ?";

        try (ResultSet resultSet = databaseConnection.executeQuery(query, username)) {
            return resultSet.next();
        }
    }

    public boolean movieExists(String movie) throws SQLException {
        String query = "SELECT 1 FROM public.movies WHERE title = ?";
        try (ResultSet resultSet = databaseConnection.executeQuery(query, movie)) {
            return resultSet.next();
        }
    }

    public List<Movie> findMovies(String genre, Integer minRating, Integer maxRating) throws SQLException {
        List<Movie> movies = new ArrayList<>();

        String query = "SELECT title, AVG(rating) FROM public.movies JOIN public.ratings ON movies.movie_id = ratings.movie_id WHERE (implicit_rating IS NOT NULL AND implicit_rating <> true)";
        if (genre != null && !genre.isEmpty()) {
            query += " AND genres ILIKE '%' || ? || '%'";
        }
        query += " GROUP BY title";
        if (minRating != null) {
            query += " HAVING AVG(rating) >= ?";
        }
        if (maxRating != null) {
            query += (minRating != null ? " AND " : " HAVING ") + "AVG(rating) <= ?";
        }

        try (ResultSet resultSet = databaseConnection.executeQuery(query, genre, minRating, maxRating);) {
            while (resultSet.next()) {
                movies.add(new Movie(resultSet.getString("title")));
            }
        }
        return movies;

    }

    public List<UserInteraction> historyByUser(String username) throws SQLException {
        ArrayList<UserInteraction> userInteractions = new ArrayList<>();
        String query = "SELECT username, title, rating, view_percentage, implicit_rating FROM public.ratings JOIN public.users ON users.user_id = ratings.user_id JOIN public.movies ON movies.movie_id = ratings.movie_id WHERE username = ?";
        try (ResultSet resultSet = databaseConnection.executeQuery(query, username)) {
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                int rating = resultSet.getInt("rating");
                int percentage = resultSet.getInt("view_percentage");
                boolean implicitRating = resultSet.getBoolean("implicit_rating");
                userInteractions.add(new UserInteraction(new Movie(title), new Rating(rating), new Percentage(percentage), implicitRating));
            }
        }
        return userInteractions;
    }

    public List<UserInteraction> historyByUsernameAndType(String username, InteractionType interactionType) throws SQLException {
        ArrayList<UserInteraction> userInteractions = new ArrayList<>();
        String query = historyQuery(interactionType);
        try (ResultSet resultSet = databaseConnection.executeQuery(query, username)) {
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                int rating = resultSet.getInt("rating");
                int percentage = resultSet.getInt("view_percentage");
                boolean implicitRating = resultSet.getBoolean("implicit_rating");
                userInteractions.add(new UserInteraction(new Movie(title), new Rating(rating), new Percentage(percentage), implicitRating));
            }
        }
        return userInteractions;
    }

    private String historyQuery(InteractionType interactionType) {
        String query = "SELECT username, title, rating, view_percentage, implicit_rating FROM public.ratings JOIN public.users ON users.user_id = ratings.user_id JOIN public.movies ON movies.movie_id = ratings.movie_id WHERE username = ? AND ";

        if (interactionType == InteractionType.RATING) {
            query += "implicit_rating = false";
        } else {
            query += "view_percentage IS NOT NULL";
        }
        return query;
    }

    public List<Movie> recommendByUsername(String username) throws SQLException {
        Set<String> favouriteGenresOfUser = getFavouriteGenresOfUser(username);
        if (favouriteGenresOfUser.isEmpty()) {
            return new ArrayList<>();
        }
        return getRecommendedMovies(username, favouriteGenresOfUser);
    }

    private Set<String> getFavouriteGenresOfUser(String username) throws SQLException {
        Set<String> favouriteGenres = new HashSet<>();
        String query = "SELECT genres FROM public.ratings JOIN public.users ON users.user_id = ratings.user_id JOIN public.movies ON movies.movie_id = ratings.movie_id WHERE username = ? AND rating >= 4 AND implicit_rating != true";
        try (ResultSet resultSet = databaseConnection.executeQuery(query, username)) {
            while (resultSet.next()) {
                String genres = resultSet.getString("genres");
                favouriteGenres.addAll(Arrays.asList(genres.split("\\|")));
            }
        }
        return favouriteGenres;
    }

    private List<Movie> getRecommendedMovies(String username, Set<String> genres) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT title\n" +
                "FROM public.movies \n" +
                "WHERE genres SIMILAR TO ?\n" +
                "EXCEPT\n" +
                "SELECT title\n" +
                "FROM public.ratings\n" +
                "JOIN public.movies\n" +
                "ON ratings.movie_id = movies.movie_id\n" +
                "JOIN public.users\n" +
                "ON users.user_id = ratings.user_id\n" +
                "WHERE username = ?";
        String pattern = "%(" + String.join("|", genres) + ")%";

        try (ResultSet resultSet = databaseConnection.executeQuery(query, pattern, username)) {
            while (resultSet.next()) {
                movies.add(new Movie(resultSet.getString("title")));
            }
        }
        return movies;
    }
}

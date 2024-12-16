package com.example.restservice;

import model.Movie;
import model.User;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class EventController {

    DatabaseConnection databaseConnection;

    public EventController() throws SQLException {
        databaseConnection = DatabaseConnection.getInstance();
    }

    public int getMovieId(String title) throws SQLException {
        String query = "SELECT movie_id FROM public.movies WHERE title = ?";
        try (ResultSet resultSet = databaseConnection.executeQuery(query, title)){
            if (resultSet.next()) {
                return resultSet.getInt("movie_id");
            } else {
                return -1;
            }
        }
    }

    public int getUserId(String username) throws SQLException {
        String query = "SELECT user_id FROM public.users WHERE username = ?";
        try (ResultSet resultSet = databaseConnection.executeQuery(query, username)) {
            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            } else {
                return -1;
            }
        }
    }

    boolean ratingExists(User user, Movie movie) throws SQLException {
        ResultSet rating = getRating(user, movie);
        return rating.next();
    }

    ResultSet getRating(User user, Movie movie) throws SQLException {
        String query = "SELECT * FROM public.users AS users JOIN public.ratings AS ratings ON users.user_id = ratings.user_id JOIN public.movies AS movies ON movies.movie_id = ratings.movie_id WHERE username = ? AND title = ?";
        return databaseConnection.executeQuery(query, user.getUsername(), movie.getTitle());
    }

}

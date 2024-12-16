package com.example.restservice;

import model.Movie;
import model.Percentage;
import model.User;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ViewEventController extends EventController {

    public ViewEventController() throws SQLException {
        super();
    }

    public void saveView(User user, Movie movie, Percentage percentage) throws SQLException {
        int movieId = getMovieId(movie.getTitle());
        int userId = getUserId(user.getUsername());
        if (ratingExists(user, movie)) {
            updatePercentage(userId, movieId, percentage.getValue());
        } else {
            storeRatingWithPercentage(userId, movieId, percentage.toRating().getRatingValue(), percentage.getValue());
        }
    }

    protected void updatePercentage(int userId, int movieId, int percentage) throws SQLException {
        String query = "UPDATE public.ratings SET view_percentage = ? WHERE user_id = ? AND movie_id = ?";
        databaseConnection.updateOnlyPercentageQuery(query, userId, movieId, percentage);
    }

    protected void storeRatingWithPercentage(int userId, int movieId, int rating, int viewPercentage) throws SQLException {
        String query = "INSERT INTO public.ratings (user_id, movie_id, rating, view_percentage, implicit_rating) VALUES (?, ?, ?, ?, true)";
        databaseConnection.insertQuery(query, userId, movieId, rating, viewPercentage);
    }
}

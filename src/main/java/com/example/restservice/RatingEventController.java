package com.example.restservice;

import model.Movie;
import model.Rating;
import model.User;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class RatingEventController extends EventController {

    public RatingEventController() throws SQLException {
        super();
    }

    public void saveRating(User user, Movie movie, Rating rating) throws SQLException {
        int movieId = getMovieId(movie.getTitle());
        int userId = getUserId(user.getUsername());
        if (ratingExists(user, movie)) {
            updateRating(userId, movieId, rating.getRatingValue(), false);
        } else {
            storeRating(userId, movieId, rating.getRatingValue());
        }
    }

    protected void updateRating(int userId, int movieId, int rating, boolean implicitRating) throws SQLException {
        String query = "UPDATE public.ratings SET rating = ?, implicit_rating = ? WHERE user_id = ? AND movie_id = ?";
        databaseConnection.updateRatingQuery(query, rating, implicitRating, userId, movieId);
    }

    protected void storeRating(int userId, int movieId, int rating) throws SQLException {
        String query = "INSERT INTO public.ratings (user_id, movie_id, rating, implicit_rating) VALUES (?, ?, ?, false)";
        databaseConnection.insertQuery(query, userId, movieId, rating);
    }

}

package model;

import java.util.Optional;

public class UserInteraction {

    private String movieTitle;
    private Integer rating;
    private Optional<Integer> viewPercentage;
    private Optional<Boolean> implicitRating;

    public UserInteraction(String movie, Integer rating, Integer viewPercentage, Boolean implicitRating) {
        this.movieTitle = movie;
        this.rating = rating;
        this.viewPercentage = Optional.ofNullable(viewPercentage);
        this.implicitRating = Optional.ofNullable(implicitRating);
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getRating() {
        return rating;
    }

    public Optional<Integer> getPercentage() {
        return viewPercentage;
    }

    public Optional<Boolean> getImplicitRating() {
        return implicitRating;
    }
}

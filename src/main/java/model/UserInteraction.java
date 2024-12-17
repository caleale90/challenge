package model;

import java.util.Optional;

public class UserInteraction {

    private String movieTitle;
    private Integer rating;
    private Optional<Integer> percentage;
    private Optional<Boolean> implicitRating;

    public UserInteraction(String movie, Integer rating, Integer percentage, Boolean implicitRating) {
        this.movieTitle = movie;
        this.rating = rating;
        this.percentage = Optional.ofNullable(percentage);
        this.implicitRating = Optional.ofNullable(implicitRating);
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getRating() {
        return rating;
    }

    public Optional<Integer> getPercentage() {
        return percentage;
    }

    public Optional<Boolean> getImplicitRating() {
        return implicitRating;
    }
}

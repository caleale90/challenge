package model;

public class UserInteraction {

    private Movie movie;
    private Rating rating;
    private Percentage percentage;
    private boolean implicitRating;

    public UserInteraction(Movie movie, Rating rating, Percentage percentage, boolean implicitRating) {
        this.movie = movie;
        this.rating = rating;
        this.percentage = percentage;
        this.implicitRating = implicitRating;
    }

    public Movie getMovie() {
        return movie;
    }

    public Rating getRating(){
        return rating;
    }

    public Percentage getPercentage(){
        return percentage;
    }

    public boolean getImplicitRating(){
        return implicitRating;
    }

    @Override
    public String toString() {
        return "UserRating{" +
                "movie=" + (movie != null ? movie.getTitle() : "N/A") +
                ", rating=" + (rating != null ? rating : "N/A") +
                ", percentage=" + (percentage != null ? percentage.getValue() : "N/A") +
                '}';
    }

}

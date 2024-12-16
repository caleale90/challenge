package model;

public class Rating {

    private int rating;

    public Rating(int rating) {
        this.rating = rating;
    }

    public int getRatingValue(){
        return rating;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "rating=" + rating +
                '}';
    }
}

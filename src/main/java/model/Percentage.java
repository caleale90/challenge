package model;

public class Percentage {

    private int value;

    public Percentage(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Rating toRating() {
        int rating;
        if (value > 80)
            rating = 5;
        else if (value > 60) {
            rating = 4;
        } else if (value > 40) {
            rating = 3;
        } else if (value > 20) {
            rating = 2;
        } else {
            rating = 1;
        }
        return new Rating(rating);
    }
}

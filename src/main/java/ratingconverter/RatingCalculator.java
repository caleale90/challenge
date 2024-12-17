package ratingconverter;

public class RatingCalculator {

    public static int calculateRating(Integer viewPercentage) {
        if (viewPercentage == null || viewPercentage < 1) {
            return 1;
        }
        if (viewPercentage <= 20) {
            return 1;
        } else if (viewPercentage <= 40) {
            return 2;
        } else if (viewPercentage <= 60) {
            return 3;
        } else if (viewPercentage <= 80) {
            return 4;
        } else {
            return 5;
        }
    }
}
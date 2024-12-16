package validation;

public class RatingValidator implements Validation {

    private int rating;

    public RatingValidator(int rating) {
        this.rating = rating;
    }

    public boolean isValid() {
        return rating > 0 && rating < 6;
    }

}

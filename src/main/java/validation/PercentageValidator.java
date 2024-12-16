package validation;

public class PercentageValidator implements Validation {

    int percentage;

    public PercentageValidator(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean isValid() {
        return percentage >= 0 && percentage <= 100;
    }

}

package validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingValidatorTest {

    @Test
    void testIsValid(){
        RatingValidator ratingValidator = new RatingValidator(3);
        assertTrue(ratingValidator.isValid());

        ratingValidator = new RatingValidator(0);
        assertFalse(ratingValidator.isValid());

        ratingValidator = new RatingValidator(6);
        assertFalse(ratingValidator.isValid());
    }

}
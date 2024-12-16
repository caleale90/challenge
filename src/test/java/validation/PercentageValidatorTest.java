package validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PercentageValidatorTest {

    @Test
    void testIsValid() {
        PercentageValidator percentageValidator = new PercentageValidator(50);
        assertTrue(percentageValidator.isValid());

        percentageValidator = new PercentageValidator(-1);
        assertFalse(percentageValidator.isValid());

        percentageValidator = new PercentageValidator(101);
        assertFalse(percentageValidator.isValid());
    }

}
package ratingconverter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingCalculatorTest {

    @Test
    void testCalculateRating() {
        assertEquals(1, RatingCalculator.calculateRating(0));
        assertEquals(1, RatingCalculator.calculateRating(20));
        assertEquals(2, RatingCalculator.calculateRating(40));
        assertEquals(3, RatingCalculator.calculateRating(60));
        assertEquals(4, RatingCalculator.calculateRating(80));
        assertEquals(5, RatingCalculator.calculateRating(100));
    }

}
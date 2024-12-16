package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    @Test
    void getRating() {
        Rating rating = new Rating(3);
        assertEquals(3, rating.getRatingValue());
    }
}
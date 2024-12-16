package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserInteractionTest {

    private static UserInteraction userInteraction;

    @BeforeAll
    static void setup() {
        userInteraction = new UserInteraction(new Movie("movie"), new Rating(3), new Percentage(50), true);
    }

    @Test
    void getMovie() {
        assertEquals("movie", userInteraction.getMovie().getTitle());
    }

    @Test
    void getRating() {
        assertEquals(3, userInteraction.getRating().getRatingValue());
    }

    @Test
    void getPercentage() {
        assertEquals(50, userInteraction.getPercentage().getValue());
    }

    @Test
    void getImplicitRating() {
        assertEquals(true, userInteraction.getImplicitRating());
    }

    @Test
    void testToString() {
        assertEquals("UserRating{movie=movie, rating=Rating{rating=3}, percentage=50}", userInteraction.toString());
    }
}
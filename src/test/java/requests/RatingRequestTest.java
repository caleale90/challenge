package requests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RatingRequestTest {

    @Test
    void testConstructorAndGetters() {
        String user = "user123";
        String movie = "Movie Title";
        int rating = 4;

        RatingRequest ratingRequest = new RatingRequest(user, movie, rating);

        assertEquals(user, ratingRequest.user, "User should be 'user123'");
        assertEquals(movie, ratingRequest.movie, "Movie should be 'Movie Title'");
        assertEquals(rating, ratingRequest.rating, "Rating should be 4");
    }

    @Test
    void testSettersAndGetters() {
        RatingRequest ratingRequest = new RatingRequest("user123", "Movie Title", 4);

        assertEquals("user123", ratingRequest.user);
        assertEquals("Movie Title", ratingRequest.movie);
        assertEquals(4, ratingRequest.rating);

        ratingRequest.user = "user456";
        ratingRequest.movie = "Another Movie";
        ratingRequest.rating = 5;

        assertEquals("user456", ratingRequest.user);
        assertEquals("Another Movie", ratingRequest.movie);
        assertEquals(5, ratingRequest.rating);
    }
}

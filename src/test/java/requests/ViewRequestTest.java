package requests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewRequestTest {

    @Test
    void testConstructorAndGetters() {
        String user = "user123";
        String movie = "Movie Title";
        int percentage = 75;

        ViewRequest viewRequest = new ViewRequest(user, movie, percentage);

        assertEquals(user, viewRequest.user, "User should be 'user123'");
        assertEquals(movie, viewRequest.movie, "Movie should be 'Movie Title'");
        assertEquals(percentage, viewRequest.percentage, "Percentage should be 75");
    }

    @Test
    void testSettersAndGetters() {
        ViewRequest viewRequest = new ViewRequest("user123", "Movie Title", 75);

        assertEquals("user123", viewRequest.user);
        assertEquals("Movie Title", viewRequest.movie);
        assertEquals(75, viewRequest.percentage);

        viewRequest.user = "user456";
        viewRequest.movie = "Another Movie";
        viewRequest.percentage = 50;

        assertEquals("user456", viewRequest.user);
        assertEquals("Another Movie", viewRequest.movie);
        assertEquals(50, viewRequest.percentage);
    }
}

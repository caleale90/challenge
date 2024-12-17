package model;

import com.example.restservice.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie("Fake title", "Genre1|Genre2");
    }

    @Test
    void getTitle() {
        assertEquals("Fake title", movie.getTitle());
    }

    @Test
    void testToString() {
        assertEquals("Movie[title='Fake title', genres='Genre1|Genre2']", movie.toString());
    }
}
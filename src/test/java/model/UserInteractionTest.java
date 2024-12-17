package model;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserInteractionTest {

    @Test
    void testConstructorWithAllValues() {
        String movieTitle = "Inception";
        int rating = 5;
        int percentage = 85;
        boolean implicitRating = true;

        UserInteraction interaction = new UserInteraction(movieTitle, rating, percentage, implicitRating);

        assertEquals(movieTitle, interaction.getMovieTitle());
        assertEquals(rating, interaction.getRating());
        assertEquals(Optional.of(percentage), interaction.getPercentage());
        assertEquals(Optional.of(implicitRating), interaction.getImplicitRating());
    }

    @Test
    void testConstructorWithNullPercentageAndImplicitRating() {
        String movieTitle = "Interstellar";
        int rating = 4;

        UserInteraction interaction = new UserInteraction(movieTitle, rating, null, null);

        assertEquals(movieTitle, interaction.getMovieTitle());
        assertEquals(rating, interaction.getRating());
        assertEquals(Optional.empty(), interaction.getPercentage());
        assertEquals(Optional.empty(), interaction.getImplicitRating());
    }
}

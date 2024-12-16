package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PercentageTest {

    @Test
    void toRating() {
        Percentage p1 = new Percentage(19);
        assertEquals(1, p1.toRating().getRatingValue());

        Percentage p2 = new Percentage(39);
        assertEquals(2, p2.toRating().getRatingValue());

        Percentage p3 = new Percentage(59);
        assertEquals(3, p3.toRating().getRatingValue());

        Percentage p4 = new Percentage(79);
        assertEquals(4, p4.toRating().getRatingValue());

        Percentage p5 = new Percentage(99);
        assertEquals(5, p5.toRating().getRatingValue());
    }

    @Test
    void getValue() {
        Percentage percentage = new Percentage(50);
        assertEquals(50, percentage.getValue());
    }
}
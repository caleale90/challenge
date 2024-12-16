package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getUsername() {
        User user = new User("fake name");
        assertEquals("fake name", user.getUsername());
    }
}
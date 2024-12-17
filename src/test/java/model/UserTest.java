package model;

import com.example.restservice.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGetUsername() {
        User user = new User("fake name");
        assertEquals("fake name", user.getUsername());
    }

    @Test
    void testToString(){
        User user = new User("fake name");
        assertEquals("User[id=null, username='fake name']", user.toString());
    }
}
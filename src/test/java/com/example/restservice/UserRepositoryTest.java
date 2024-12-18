package com.example.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("JohnDoe");
        userRepository.save(user);
    }

    @Test
    public void testFindUserIdByUsernameWhenUserExists() {
        Optional<Long> userIdOptional = userRepository.findUserIdByUsername("JohnDoe");

        assertTrue(userIdOptional.isPresent(), "User ID should be found for the given username.");
        assertEquals(user.getId(), userIdOptional.get(), "The retrieved User ID should match the saved User ID.");
    }

    @Test
    public void testFindUserIdByUsernameWhenUserDoesNotExist() {
        Optional<Long> userIdOptional = userRepository.findUserIdByUsername("JaneDoe");

        assertFalse(userIdOptional.isPresent(), "No User ID should be found for a nonexistent username.");
    }
}

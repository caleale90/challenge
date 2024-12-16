package validation;

import com.example.restservice.DatabaseController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsernameValidatorTest {

    @Mock
    private DatabaseController mockDatabaseController;

    @InjectMocks
    private UsernameValidator usernameValidator;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        usernameValidator = new UsernameValidator("username");
        ReflectionTestUtils.setField(usernameValidator, "dbController", mockDatabaseController);
    }

    @Test
    void testIsValid() throws SQLException {
        when(mockDatabaseController.usernameExists("username")).thenReturn(true);

        assertTrue(usernameValidator.isValid());

        verify(mockDatabaseController,times(1)).usernameExists("username");
    }

    @Test
    void testIsValidWhenUsernameNotFound() throws SQLException {
        when(mockDatabaseController.usernameExists("username")).thenReturn(false);

        assertFalse(usernameValidator.isValid());

        verify(mockDatabaseController,times(1)).usernameExists("username");
    }

}
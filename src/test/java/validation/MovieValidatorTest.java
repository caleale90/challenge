package validation;

import com.example.restservice.DatabaseController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class MovieValidatorTest {

    @Mock
    private DatabaseController mockDbController;

    private String movieTitle = "Test Movie";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsValidwhenMovieExists() throws SQLException {
        when(mockDbController.movieExists(movieTitle)).thenReturn(true);
        MovieValidator validator = new MovieValidator(movieTitle);
        ReflectionTestUtils.setField(validator, "dbController", mockDbController);

        assertTrue(validator.isValid());
    }

    @Test
    void testIsValidwhenMovieDoesNotExist() throws SQLException {
        when(mockDbController.movieExists(movieTitle)).thenReturn(false);

        MovieValidator validator = new MovieValidator(movieTitle);
        ReflectionTestUtils.setField(validator, "dbController", mockDbController);

        assertFalse(validator.isValid(), "isValid should return false if the movie does not exist");
    }

    @Test
    void testIsValid_whenSQLExceptionThrown_returnsFalse() throws SQLException {
        when(mockDbController.movieExists(movieTitle)).thenThrow(new SQLException("Database error"));

        MovieValidator validator = new MovieValidator(movieTitle);
        ReflectionTestUtils.setField(validator, "dbController", mockDbController);

        assertFalse(validator.isValid(), "isValid should return false if a SQLException is thrown");
    }
}

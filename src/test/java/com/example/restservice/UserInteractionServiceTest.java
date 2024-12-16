package com.example.restservice;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserInteractionServiceTest {

    @Mock
    private DatabaseController mockDatabaseController;

    private UserInteractionService userInteractionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userInteractionService = new UserInteractionService();
        ReflectionTestUtils.setField(userInteractionService, "databaseController", mockDatabaseController);
    }

    @Test
    public void testGetUserInteractions_NoType() throws SQLException {
        String username = "testUser";
        List<UserInteraction> mockInteractions = Arrays.asList(
                new UserInteraction(new Movie("1"), new Rating(1), new Percentage(1), true));

        when(mockDatabaseController.historyByUser(username)).thenReturn(mockInteractions);

        List<UserInteraction> interactions = userInteractionService.getUserInteractions(username, "");

        assertNotNull(interactions);
        assertEquals(1, interactions.size());
        verify(mockDatabaseController, times(1)).historyByUser(username);
    }

    @Test
    public void testGetUserInteractions_Ratings() throws SQLException {
        String username = "testUser";
        List<UserInteraction> mockInteractions = Arrays.asList(
                new UserInteraction(new Movie("1"), new Rating(1), new Percentage(1), true),
                new UserInteraction(new Movie("2"), new Rating(2), new Percentage(2), true));

        when(mockDatabaseController.historyByUsernameAndType(username, InteractionType.RATING)).thenReturn(mockInteractions);

        List<UserInteraction> interactions = userInteractionService.getUserInteractions(username, "ratings");

        assertNotNull(interactions);
        assertEquals(2, interactions.size());
        verify(mockDatabaseController, times(1)).historyByUsernameAndType(username, InteractionType.RATING);
    }

    @Test
    public void testGetUserInteractions_Views() throws SQLException {
        String username = "testUser";
        List<UserInteraction> mockInteractions = Arrays.asList(
                new UserInteraction(new Movie("1"), new Rating(1), new Percentage(1), true),
                new UserInteraction(new Movie("2"), new Rating(2), new Percentage(2), true)
                );

        when(mockDatabaseController.historyByUsernameAndType(username, InteractionType.VIEW)).thenReturn(mockInteractions);

        List<UserInteraction> interactions = userInteractionService.getUserInteractions(username, "views");

        assertNotNull(interactions);
        assertEquals(2, interactions.size());
        verify(mockDatabaseController, times(1)).historyByUsernameAndType(username, InteractionType.VIEW);
    }

    @Test
    public void testGetUserInteractions_InvalidType() {
        String username = "testUser";
        String invalidType = "invalidType";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userInteractionService.getUserInteractions(username, invalidType);
        });

        assertEquals("Invalid interaction type: " + invalidType, exception.getMessage());
    }
}

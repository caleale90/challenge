package com.example.restservice;

import model.InteractionType;
import model.UserInteraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserInteractionServiceTest {

    @Mock
    RatingRepository ratingRepository;

    @InjectMocks
    UserInteractionService userInteractionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserInteractionsRatingType() {
        String username = "john_doe";
        List<UserInteraction> mockInteractions = new ArrayList<>();
        mockInteractions.add(new UserInteraction("Movie1", 5, 80, true));
        mockInteractions.add(new UserInteraction("Movie2", 3, 90, false));

        when(ratingRepository.historyByUsernameAndType(username, InteractionType.RATING.toString())).thenReturn(mockInteractions);

        List<UserInteraction> interactions = userInteractionService.getUserInteractions(username, "ratings");

        assertEquals(2, interactions.size());
        assertEquals("Movie1", interactions.get(0).getMovieTitle());
        assertEquals(5, interactions.get(0).getRating());
        assertEquals(Optional.of(80), interactions.get(0).getPercentage());
        assertEquals(Optional.of(true), interactions.get(0).getImplicitRating());

        assertEquals("Movie2", interactions.get(1).getMovieTitle());
        assertEquals(3, interactions.get(1).getRating());
        assertEquals(Optional.of(90), interactions.get(1).getPercentage());
        assertEquals(Optional.of(false), interactions.get(1).getImplicitRating());
    }

    @Test
    void testGetUserInteractionsWithViewsType() {
        String username = "john_doe";
        List<UserInteraction> mockInteractions = new ArrayList<>();
        mockInteractions.add(new UserInteraction("Movie3", 0, 100, null));
        mockInteractions.add(new UserInteraction("Movie4", 0, 95, null));

        when(ratingRepository.historyByUsernameAndType(username, InteractionType.VIEW.toString())).thenReturn(mockInteractions);

        List<UserInteraction> interactions = userInteractionService.getUserInteractions(username, "views");

        assertEquals(2, interactions.size());
        assertEquals("Movie3", interactions.get(0).getMovieTitle());
        assertEquals(0, interactions.get(0).getRating());
        assertEquals(Optional.of(100), interactions.get(0).getPercentage());
        assertEquals(Optional.empty(), interactions.get(0).getImplicitRating());

        assertEquals("Movie4", interactions.get(1).getMovieTitle());
        assertEquals(0, interactions.get(1).getRating());
        assertEquals(Optional.of(95), interactions.get(1).getPercentage());
        assertEquals(Optional.empty(), interactions.get(1).getImplicitRating());
    }

    @Test
    void testGetUserInteractionsWithInvalidType() {
        String username = "john_doe";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userInteractionService.getUserInteractions(username, "invalidType")
        );

        assertEquals("Invalid interaction type: invalidType", exception.getMessage());
    }

    @Test
    void testGetUserInteractions_withNullType() {
        String username = "john_doe";
        List<UserInteraction> mockInteractions = new ArrayList<>();
        mockInteractions.add(new UserInteraction("Movie5", 4, 70, true));

        when(ratingRepository.historyByUser(username)).thenReturn(mockInteractions);

        List<UserInteraction> interactions = userInteractionService.getUserInteractions(username, null);

        assertEquals(1, interactions.size());
        assertEquals("Movie5", interactions.get(0).getMovieTitle());
        assertEquals(4, interactions.get(0).getRating());
        assertEquals(Optional.of(70), interactions.get(0).getPercentage());
        assertEquals(Optional.of(true), interactions.get(0).getImplicitRating());
    }

    @Test
    void testGetUserInteractions_withEmptyType() {
        String username = "john_doe";
        List<UserInteraction> mockInteractions = new ArrayList<>();
        mockInteractions.add(new UserInteraction("Movie6", 2, 60, false));

        when(ratingRepository.historyByUser(username)).thenReturn(mockInteractions);

        List<UserInteraction> interactions = userInteractionService.getUserInteractions(username, "");

        assertEquals(1, interactions.size());
        assertEquals("Movie6", interactions.get(0).getMovieTitle());
        assertEquals(2, interactions.get(0).getRating());
        assertEquals(Optional.of(60), interactions.get(0).getPercentage());
        assertEquals(Optional.of(false), interactions.get(0).getImplicitRating());
    }
}

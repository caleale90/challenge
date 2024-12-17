package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationSystem {

    @Autowired
    RatingRepository ratingRepository;

    public Set<String> recommendMovies(String username) {
        List<String> favouriteGenresByUsername = ratingRepository.findFavouriteGenresByUsername(username);
        Set<String> favouriteGenres = extractGenres(favouriteGenresByUsername);
        if (favouriteGenres.isEmpty()) {
            return new HashSet<>();
        }
        String pattern = "%(" + String.join("|", favouriteGenres) + ")%";
        Set<String> otherMoviesOfSameGenres = ratingRepository.findRecommendedMovies(pattern);
        Set<String> ratedFilms = ratingRepository.findRatedFilms(username);
        otherMoviesOfSameGenres.removeAll(ratedFilms);
        return otherMoviesOfSameGenres;
    }

    protected Set<String> extractGenres(List<String> favouriteGenresByUsername) {
        Set<String> favouriteGenres = new HashSet<>();
        for (String g : favouriteGenresByUsername) {
            favouriteGenres.addAll(Arrays.asList(g.split("\\|")));
        }
        return favouriteGenres;
    }
}

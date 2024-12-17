package com.example.restservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void findByTitle() {
        Movie movie = new Movie("Inception", "Action|Sci-Fi");
        movieRepository.save(movie);

        List<Movie> movies = movieRepository.findByTitle("Inception");

        assertThat(movies).isNotEmpty();
        assertThat(movies.get(0).getTitle()).isEqualTo("Inception");
    }

    @Test
    void findByGenre() {
        Movie movie = new Movie("The Matrix", "Action|Sci-Fi");
        movieRepository.save(movie);

        List<Movie> movies = movieRepository.findByGenre("Sci-Fi");

        assertThat(movies).isNotEmpty();
        assertThat(movies.get(0).getTitle()).isEqualTo("The Matrix");
    }

    @Test
    void findIdByTitle() {
        Movie movie = new Movie("Avatar","Action|Adventure");
        Movie savedMovie = movieRepository.save(movie);

        Optional<Long> movieId = movieRepository.findIdByTitle("Avatar");

        assertThat(movieId).isPresent();
        assertThat(movieId.get()).isEqualTo(savedMovie.getId());
    }
}

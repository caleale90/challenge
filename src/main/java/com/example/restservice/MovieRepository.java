package com.example.restservice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Long> {

    List<Movie> findByTitle(String title);

    @Query("SELECT m FROM Movie m WHERE m.genres LIKE %:genre%")
    List<Movie> findByGenre(@Param("genre") String genre);

    @Query("SELECT m.id FROM Movie m WHERE m.title = :title")
    Optional<Long> findIdByTitle(@Param("title") String title);

}

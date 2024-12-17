package com.example.restservice;

import jakarta.persistence.*;


@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genres;

    protected Movie() {
    }

    public Movie(String title, String genres) {
        this.title = title;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return String.format(
                "Movie[title='%s', genres='%s']",
                title, genres);
    }

    public long getId() {
        return id;
    }
}

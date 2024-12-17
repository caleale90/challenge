package com.example.restservice;

import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    private Integer rating;
    private Integer viewPercentage;
    private Boolean implicitRating;

    protected Rating() {}

    public Rating(User user, Movie movie, Integer rating, Integer viewPercentage, Boolean implicitRating) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.viewPercentage = viewPercentage;
        this.implicitRating = implicitRating;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getRating() {
        return rating;
    }

    public int getViewPercentage() {
        return viewPercentage;
    }

    public boolean getImplicitRating() {
        return implicitRating;
    }
}


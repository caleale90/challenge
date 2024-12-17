package com.example.restservice;

import jakarta.transaction.Transactional;
import model.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT r FROM Rating r " +
            "JOIN r.user u " +
            "JOIN r.movie m " +
            "WHERE u.id = :userId AND m.id = :movieId")
    List<Rating> findRatingsByUserAndMovie(@Param("userId") Long userId, @Param("movieId") Long movieId);

    @Modifying
    @Transactional
    @Query("UPDATE Rating r SET r.rating = :rating, r.implicitRating = :implicitRating " +
            "WHERE r.user.id = :userId AND r.movie.id = :movieId")
    void updateRatingAndImplicitRating(@Param("rating") Integer rating,
                                       @Param("implicitRating") Boolean implicitRating,
                                       @Param("userId") Long userId,
                                       @Param("movieId") Long movieId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO public.ratings (user_id, movie_id, rating, implicit_rating) " +
            "VALUES (:userId, :movieId, :rating, false)", nativeQuery = true)
    void insertRating(@Param("userId") Long userId, @Param("movieId") Long movieId, @Param("rating") int rating);

    @Modifying
    @Transactional
    @Query("UPDATE Rating r SET r.viewPercentage = :viewPercentage WHERE r.user.id = :userId AND r.movie.id = :movieId")
    void updateViewPercentage(@Param("userId") Long userId, @Param("movieId") Long movieId, @Param("viewPercentage") int viewPercentage);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO public.ratings (user_id, movie_id, rating, implicit_rating, view_percentage) " +
            "VALUES (:userId, :movieId, :rating, true, :viewPercentage)", nativeQuery = true)
    void insertRatingWithPercentage(@Param("userId") Long userId, @Param("movieId") Long movieId,
                                    @Param("rating") Integer rating, @Param("viewPercentage") Integer viewPercentage);

    @Query("SELECT m.title, AVG(r.rating) " +
            "FROM Movie m " +
            "LEFT OUTER JOIN Rating r ON m.id = r.movie.id " +
            "WHERE (:genre IS NULL OR m.genres LIKE %:genre%) " +
            "GROUP BY m.title " +
            "HAVING " +
            "  (:minRating IS NULL OR AVG(r.rating) >= :minRating) " +
            "  AND (:maxRating IS NULL OR AVG(r.rating) <= :maxRating)")
    List<Object[]> findMovies(@Param("genre") String genre,
                              @Param("minRating") Integer minRating,
                              @Param("maxRating") Integer maxRating);

    @Query("SELECT new model.UserInteraction(m.title, r.rating, " +
            "CASE WHEN r.viewPercentage IS NOT NULL THEN r.viewPercentage ELSE NULL END, " +
            "CASE WHEN r.implicitRating IS NOT NULL THEN r.implicitRating ELSE NULL END) " +
            "FROM Rating r " +
            "JOIN r.user u " +
            "JOIN r.movie m " +
            "WHERE u.username = :username")
    List<UserInteraction> historyByUser(@Param("username") String username);

    @Query("SELECT new model.UserInteraction(m.title, r.rating, " +
            "CASE WHEN r.viewPercentage IS NOT NULL THEN r.viewPercentage ELSE NULL END, " +
            "CASE WHEN r.implicitRating IS NOT NULL THEN r.implicitRating ELSE NULL END) " +
            "FROM Rating r " +
            "JOIN r.user u " +
            "JOIN r.movie m " +
            "WHERE u.username = :username " +
            "AND (:interactionType = 'RATING' AND r.implicitRating = false " +
            "OR :interactionType != 'RATING' AND r.viewPercentage IS NOT NULL)")
    List<UserInteraction> historyByUsernameAndType(@Param("username") String username,
                                                   @Param("interactionType") String interactionType);


    @Query("SELECT m.genres " +
            "FROM Rating r " +
            "JOIN r.user u " +
            "JOIN r.movie m " +
            "WHERE u.username = :username " +
            "AND r.rating >= 4 " +
            "AND r.implicitRating != true")
    List<String> findFavouriteGenresByUsername(@Param("username") String username);

    @Query(value = "SELECT m.title FROM movies m " +
            "WHERE m.genres SIMILAR TO :pattern ", nativeQuery = true)
    Set<String> findRecommendedMovies(@Param("pattern") String pattern);

    @Query("SELECT m.title " +
            "FROM Rating r " +
            "JOIN r.user u " +
            "JOIN r.movie m " +
            "WHERE u.username = :username AND r.implicitRating = false")
    Set<String> findRatedFilms(@Param("username") String username);

    @Query("SELECT r.implicitRating FROM Rating r WHERE r.user.id = :userId AND r.movie.id = :movieId")
    boolean isImplicitRating(@Param("userId") Long userId, @Param("movieId") Long movieId);
}

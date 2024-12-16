package validation;

import com.example.restservice.DatabaseController;

import java.sql.SQLException;

public class MovieValidator implements Validation {

    private final String movie;
    private DatabaseController dbController;

    public MovieValidator(String movie) {
        this.movie = movie;
        this.dbController = new DatabaseController();
    }

    @Override
    public boolean isValid() {
        try {
            return dbController.movieExists(movie);
        } catch (SQLException e) {
            return false;
        }
    }
}
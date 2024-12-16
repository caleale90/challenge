package validation;

import com.example.restservice.DatabaseController;

import java.sql.SQLException;

public class UsernameValidator implements Validation {

    private String user;
    private DatabaseController dbController;

    public UsernameValidator(String user) {
        this.user = user;
        this.dbController = new DatabaseController();
    }

    public boolean isValid() {
        try {
            return dbController.usernameExists(user);
        } catch (SQLException exception) {
            return false;
        }
    }
}

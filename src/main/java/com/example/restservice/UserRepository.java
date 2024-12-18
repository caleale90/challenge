package com.example.restservice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    Optional<Long> findUserIdByUsername(@Param("username") String username);

}

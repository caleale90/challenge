package com.example.restservice;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    protected User(){}

    public User(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, username='%s']",
                id, username);
    }
}

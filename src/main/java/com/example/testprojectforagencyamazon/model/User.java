package com.example.testprojectforagencyamazon.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.UUID;

@Document(collection = "users")
@NoArgsConstructor
@Data
public class User {
    @MongoId
    private String id = UUID.randomUUID().toString();
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
    }
}

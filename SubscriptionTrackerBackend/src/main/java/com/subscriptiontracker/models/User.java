package com.subscriptiontracker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class User {
    private int id;
    private String email;
    private String username;
    
    @JsonIgnore // This prevents passwordHash from being included in JSON
    private String passwordHash;
    
    private Timestamp createdAt;
    
    // Constructors
    public User() {}
    
    public User(String email, String username, String passwordHash) {
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
    }
    
    // Getters and Setters
    
    @JsonProperty("id")
    public int getId() { return id; }
    
    public void setId(int id) { this.id = id; }
    
    @JsonProperty("email")
    public String getEmail() { return email; }
    
    public void setEmail(String email) { this.email = email; }
    
    @JsonProperty("username")
    public String getUsername() { return username; }
    
    public void setUsername(String username) { this.username = username; }
    
    @JsonIgnore // Also add to getter to ensure it's never serialized
    public String getPasswordHash() { return passwordHash; }
    
    @JsonProperty("passwordHash") // But allow setting from JSON if needed
    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }
    
    @JsonProperty("createdAt")
    public Timestamp getCreatedAt() { return createdAt; }
    
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    // Optional: Add a method to get createdAt as string
    @JsonProperty("createdAtString")
    public String getCreatedAtString() {
        return createdAt != null ? createdAt.toString() : null;
    }
}
package com.subscriptiontracker.services;

import com.subscriptiontracker.dao.UserDAO;
import com.subscriptiontracker.models.User;
import com.subscriptiontracker.utils.PasswordUtil;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthService {
    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
    private UserDAO userDAO = new UserDAO();
    
    public Map<String, Object> register(String email, String username, String password) {
    Map<String, Object> response = new HashMap<>();
    
    try {
        // Check if email exists
        if (userDAO.findByEmail(email) != null) {
            response.put("success", false);
            response.put("message", "Email already exists");
            return response;
        }
        
        // Check if username exists
        if (userDAO.findByUsername(username) != null) {
            response.put("success", false);
            response.put("message", "Username already exists");
            return response;
        }
        
        // Hash password
        String hashedPassword = PasswordUtil.hashPassword(password);
        
        // Create user
        User user = new User(email, username, hashedPassword);
        user = userDAO.createUser(user);
        
        response.put("success", true);
        response.put("message", "Registration successful");
        response.put("user", user); // Return User object
        
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Registration error", e);
        response.put("success", false);
        response.put("message", "Database error occurred");
    }
    
    return response;
}
    
   public Map<String, Object> login(String emailOrUsername, String password) {
    Map<String, Object> response = new HashMap<>();
    
    try {
        System.out.println("DEBUG AuthService: Looking for user: " + emailOrUsername);
        
        User user = userDAO.findByEmail(emailOrUsername);
        System.out.println("DEBUG AuthService: findByEmail result: " + (user != null));
        
        if (user == null) {
            user = userDAO.findByUsername(emailOrUsername);
            System.out.println("DEBUG AuthService: findByUsername result: " + (user != null));
        }
        
        if (user == null) {
            System.out.println("DEBUG AuthService: User not found");
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return response;
        }
        
        System.out.println("DEBUG AuthService: Found user - ID: " + user.getId() + 
                          ", Username: " + user.getUsername());
        System.out.println("DEBUG AuthService: Password hash: " + user.getPasswordHash());
        
        boolean passwordValid = PasswordUtil.checkPassword(password, user.getPasswordHash());
        System.out.println("DEBUG AuthService: Password check: " + passwordValid);
        
        if (!passwordValid) {
            response.put("success", false);
            response.put("message", "Invalid credentials");
            return response;
        }
        
        System.out.println("DEBUG AuthService: Login successful, returning user object");
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("user", user);
        
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Login error", e);
        e.printStackTrace();
        response.put("success", false);
        response.put("message", "Database error occurred: " + e.getMessage());
    }
    
    return response;
}
}
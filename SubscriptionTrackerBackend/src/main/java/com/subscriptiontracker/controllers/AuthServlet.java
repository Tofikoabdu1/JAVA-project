package com.subscriptiontracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subscriptiontracker.models.User;
import com.subscriptiontracker.services.AuthService;
import com.subscriptiontracker.utils.JsonResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AuthServlet.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthService authService = new AuthService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Add at the beginning of doPost, doGet, etc.
response.setHeader("Access-Control-Allow-Origin", "*");
response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
response.setHeader("Access-Control-Allow-Credentials", "true");
        
        String path = request.getPathInfo();
        
        if (path == null || path.equals("/")) {
            JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                  "Invalid endpoint");
            return;
        }
        
        switch (path) {
            case "/register":
                handleRegister(request, response);
                break;
            case "/login":
                handleLogin(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            default:
                JsonResponse.sendError(response, HttpServletResponse.SC_NOT_FOUND, 
                                      "Endpoint not found");
        }
    }
    
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
        throws IOException {
        // Add at the beginning of doPost, doGet, etc.
response.setHeader("Access-Control-Allow-Origin", "*");
response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
response.setHeader("Access-Control-Allow-Credentials", "true");
    
    try {
        Map<String, String> requestBody = objectMapper.readValue(
            request.getReader(), 
            new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {}
        );
        
        String email = requestBody.get("email");
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        
        // Validate input
        if (email == null || username == null || password == null ||
            email.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
            
            JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                  "All fields are required");
            return;
        }
        
        Map<String, Object> result = authService.register(email, username, password);
        
        if ((boolean) result.get("success")) {
            User user = (User) result.get("user"); // Cast to User
            user.setPasswordHash(null); // Remove password hash
            JsonResponse.sendSuccess(response, "Registration successful", user);
        } else {
            JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                  (String) result.get("message"));
        }
        
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Registration error", e);
        JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                              "Server error occurred");
    }
}
    
   private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
        throws IOException {
       // Add at the beginning of doPost, doGet, etc.
response.setHeader("Access-Control-Allow-Origin", "*");
response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
response.setHeader("Access-Control-Allow-Credentials", "true");
    
    try {
        Map<String, String> requestBody = objectMapper.readValue(
            request.getReader(), 
            new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {}
        );
        
        String emailOrUsername = requestBody.get("emailOrUsername");
        String password = requestBody.get("password");
        
        if (emailOrUsername == null || password == null ||
            emailOrUsername.trim().isEmpty() || password.trim().isEmpty()) {
            
            JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                  "Email/Username and password are required");
            return;
        }
        
        Map<String, Object> result = authService.login(emailOrUsername, password);
        
        if ((boolean) result.get("success")) {
            // FIX: Get the User object properly
            User user = (User) result.get("user"); // Cast to User, not Map
            
            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            
            // Remove password hash before sending response
            user.setPasswordHash(null);
            
            JsonResponse.sendSuccess(response, "Login successful", user);
        } else {
            JsonResponse.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, 
                                  (String) result.get("message"));
        }
        
    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Login error", e);
        JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                              "Server error occurred");
    }
}
    
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Add at the beginning of doPost, doGet, etc.
response.setHeader("Access-Control-Allow-Origin", "*");
response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
response.setHeader("Access-Control-Allow-Credentials", "true");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        JsonResponse.sendSuccess(response, "Logged out successfully", null);
    }
}
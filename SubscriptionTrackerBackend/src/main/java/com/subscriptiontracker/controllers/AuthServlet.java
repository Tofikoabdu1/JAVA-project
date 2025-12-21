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
    
    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        String accept = request.getHeader("Accept");
        String xrw = request.getHeader("X-Requested-With");
        return (contentType != null && contentType.contains("application/json"))
                || (accept != null && accept.contains("application/json"))
                || (xrw != null && xrw.equalsIgnoreCase("XMLHttpRequest"));
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Add at the beginning of doPost, doGet, etc.
//response.setHeader("Access-Control-Allow-Origin", "*");
//response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//response.setHeader("Access-Control-Allow-Credentials", "true");
        
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
        throws IOException, ServletException {
        try {
            String email;
            String username;
            String password;

            if (isJsonRequest(request)) {
                Map<String, String> requestBody = objectMapper.readValue(
                    request.getReader(), 
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {}
                );
                email = requestBody.get("email");
                username = requestBody.get("username");
                password = requestBody.get("password");
            } else {
                email = request.getParameter("email");
                username = request.getParameter("username");
                password = request.getParameter("password");
            }

            if (email == null || username == null || password == null ||
                email.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
                if (isJsonRequest(request)) {
                    JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "All fields are required");
                } else {
                    request.setAttribute("error", "All fields are required");
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                }
                return;
            }

            Map<String, Object> result = authService.register(email, username, password);

            if ((boolean) result.get("success")) {
                User user = (User) result.get("user");
                user.setPasswordHash(null);
                if (isJsonRequest(request)) {
                    JsonResponse.sendSuccess(response, "Registration successful", user);
                } else {
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                }
            } else {
                if (isJsonRequest(request)) {
                    JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, (String) result.get("message"));
                } else {
                    request.setAttribute("error", (String) result.get("message"));
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Registration error", e);
            if (isJsonRequest(request)) {
                JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
            } else {
                request.setAttribute("error", "Server error occurred");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        }
    }
    
   private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
        throws IOException {
    try {
        String emailOrUsername;
        String password;

        if (isJsonRequest(request)) {
            Map<String, String> requestBody = objectMapper.readValue(
                request.getReader(), 
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {}
            );
            emailOrUsername = requestBody.get("emailOrUsername");
            password = requestBody.get("password");
        } else {
            emailOrUsername = request.getParameter("emailOrUsername");
            password = request.getParameter("password");
        }

        if (emailOrUsername == null || password == null ||
            emailOrUsername.trim().isEmpty() || password.trim().isEmpty()) {
            if (isJsonRequest(request)) {
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Email/Username and password are required");
            } else {
                request.setAttribute("error", "Email/Username and password are required");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            return;
        }

        Map<String, Object> result = authService.login(emailOrUsername, password);

        if ((boolean) result.get("success")) {
            User user = (User) result.get("user");

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setMaxInactiveInterval(30 * 60);

            user.setPasswordHash(null);

            if (isJsonRequest(request)) {
                JsonResponse.sendSuccess(response, "Login successful", user);
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
            }
        } else {
            if (isJsonRequest(request)) {
                JsonResponse.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, (String) result.get("message"));
            } else {
                request.setAttribute("error", (String) result.get("message"));
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        }

    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Login error", e);
        if (isJsonRequest(request)) {
            JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
        } else {
            request.setAttribute("error", "Server error occurred");
            try {
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } catch (Exception ex) {
                // ignore forwarding exception
            }
        }
    }
}
    
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Add at the beginning of doPost, doGet, etc.
//response.setHeader("Access-Control-Allow-Origin", "*");
//response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//response.setHeader("Access-Control-Allow-Credentials", "true");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        JsonResponse.sendSuccess(response, "Logged out successfully", null);
    }
}
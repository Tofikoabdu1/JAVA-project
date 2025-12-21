package com.subscriptiontracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subscriptiontracker.dao.SubscriptionDAO;
import com.subscriptiontracker.models.Subscription;
import com.subscriptiontracker.services.DateCalculator;
import com.subscriptiontracker.utils.JsonResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/paid/*")
public class PaidServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(PaidServlet.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
    private final DateCalculator dateCalculator = new DateCalculator();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Add CORS headers
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        // DEBUG: Log request info
        System.out.println("=== PAID SERVLET DEBUG ===");
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Path Info: " + request.getPathInfo());
        
        // FIX: Check if session exists - use getSession(false) to not create new session
        HttpSession session = request.getSession(false);
        if (session == null) {
            System.out.println("DEBUG: No session found - user not logged in");
            JsonResponse.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, 
                                  "Please login first");
            return;
        }
        
        // FIX: Check if userId attribute exists in session
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            System.out.println("DEBUG: No userId in session - session expired");
            JsonResponse.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, 
                                  "Session expired, please login again");
            return;
        }
        
        int userId = (int) userIdObj;
        System.out.println("DEBUG: User ID from session: " + userId);
        System.out.println("DEBUG: Username from session: " + session.getAttribute("username"));
        
        try {
            String pathInfo = request.getPathInfo();
            System.out.println("DEBUG: Path Info: " + pathInfo);
            
            if (pathInfo == null || pathInfo.equals("/")) {
                System.out.println("DEBUG: No subscription ID in path");
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Subscription ID required");
                return;
            }
            
            // Extract subscription ID from path like "/123"
            String[] parts = pathInfo.split("/");
            System.out.println("DEBUG: Path parts: " + java.util.Arrays.toString(parts));
            
            if (parts.length < 2) {
                System.out.println("DEBUG: Invalid path format");
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Invalid URL format");
                return;
            }
            
            int subscriptionId;
            try {
                subscriptionId = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("DEBUG: Invalid subscription ID format: " + parts[1]);
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Invalid subscription ID format");
                return;
            }
            
            System.out.println("DEBUG: Looking for subscription ID: " + subscriptionId + " for user ID: " + userId);
            
            // Get subscription
            Subscription subscription = subscriptionDAO.findById(subscriptionId, userId);
            if (subscription == null) {
                System.out.println("DEBUG: Subscription not found");
                JsonResponse.sendError(response, HttpServletResponse.SC_NOT_FOUND, 
                                      "Subscription not found or you don't have permission");
                return;
            }
            
            System.out.println("DEBUG: Found subscription: " + subscription.getName());
            System.out.println("DEBUG: Current due date: " + subscription.getNextDueDate());
            
            // Calculate new due date (one month forward)
            Date currentDueDate = subscription.getNextDueDate();
            Date newDueDate = dateCalculator.markAsPaid(currentDueDate);
            
            System.out.println("DEBUG: New due date: " + newDueDate);
            
            // Update in database
            boolean updated = subscriptionDAO.updateNextDueDate(subscriptionId, userId, newDueDate);
            
            if (updated) {
                // Update the subscription object with new date
                subscription.setNextDueDate(newDueDate);
                
                // Create response with readable date strings
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("id", subscription.getId());
                responseData.put("userId", subscription.getUserId());
                responseData.put("name", subscription.getName());
                responseData.put("amount", subscription.getAmount());
                responseData.put("dueDay", subscription.getDueDay());
                responseData.put("nextDueDate", subscription.getNextDueDate().toString());
                responseData.put("createdAt", subscription.getCreatedAt() != null ? 
                    subscription.getCreatedAt().toString() : null);
                responseData.put("updatedAt", subscription.getUpdatedAt() != null ? 
                    subscription.getUpdatedAt().toString() : null);
                
                System.out.println("DEBUG: Successfully marked as paid");
                JsonResponse.sendSuccess(response, "Subscription marked as paid", responseData);
                
            } else {
                System.out.println("DEBUG: Database update failed");
                JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                      "Failed to mark subscription as paid");
            }
            
        } catch (SQLException e) {
            System.err.println("DEBUG: SQL Exception: " + e.getMessage());
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Error marking subscription as paid", e);
            JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                  "Database error occurred");
        } catch (Exception e) {
            System.err.println("DEBUG: Unexpected error: " + e.getMessage());
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Unexpected error in PaidServlet", e);
            JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                  "Server error occurred");
        }
    }
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Handle CORS preflight requests
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
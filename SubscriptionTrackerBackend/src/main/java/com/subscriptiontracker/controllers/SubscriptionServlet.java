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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/subscriptions/*")
public class SubscriptionServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SubscriptionServlet.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
    private final DateCalculator dateCalculator = new DateCalculator();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        int userId = (int) session.getAttribute("userId");
        
        try {
            List<Subscription> subscriptions = subscriptionDAO.findByUserId(userId);
            JsonResponse.sendSuccess(response, "Subscriptions retrieved", subscriptions);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving subscriptions", e);
            JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                  "Error retrieving subscriptions");
        }
    }
    
//    @Override
//   package com.subscriptiontracker.controllers;



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        int userId = (int) session.getAttribute("userId");
        
        try {
            Map<String, Object> requestBody = objectMapper.readValue(
                request.getReader(), 
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {}
            );
            
            String name = (String) requestBody.get("name");
            Object amountObj = requestBody.get("amount");
            Object dueDayObj = requestBody.get("due_day");
            
            // Validate input
            if (name == null || name.trim().isEmpty()) {
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Name is required");
                return;
            }
            
            double amount = 0;
            int dueDay = 0;
            
            try {
                if (amountObj instanceof Number) {
                    amount = ((Number) amountObj).doubleValue();
                } else if (amountObj instanceof String) {
                    amount = Double.parseDouble((String) amountObj);
                }
                
                if (dueDayObj instanceof Number) {
                    dueDay = ((Number) dueDayObj).intValue();
                } else if (dueDayObj instanceof String) {
                    dueDay = Integer.parseInt((String) dueDayObj);
                }
            } catch (NumberFormatException e) {
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Invalid number format");
                return;
            }
            
            if (amount <= 0) {
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Amount must be positive");
                return;
            }
            
            if (dueDay < 1 || dueDay > 31) {
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Due day must be between 1 and 31");
                return;
            }
            
            // Create subscription
            Subscription subscription = new Subscription(userId, name, amount, dueDay);
            
            // Calculate initial due date
            Date nextDueDate = dateCalculator.calculateNextDueDate(dueDay);
            subscription.setNextDueDate(nextDueDate);
            
            subscription = subscriptionDAO.create(subscription);
            
            JsonResponse.sendSuccess(response, "Subscription created successfully", subscription);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating subscription", e);
            JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                  "Error creating subscription");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                  "Invalid request format");
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        int userId = (int) session.getAttribute("userId");
        
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Subscription ID required");
                return;
            }
            
            String[] parts = pathInfo.split("/");
            if (parts.length < 2) {
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Invalid URL format");
                return;
            }
            
            int subscriptionId = Integer.parseInt(parts[1]);
            
            Map<String, Object> requestBody = objectMapper.readValue(
                request.getReader(), 
                new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {}
            );
            
            // Get existing subscription
            Subscription subscription = subscriptionDAO.findById(subscriptionId, userId);
            if (subscription == null) {
                JsonResponse.sendError(response, HttpServletResponse.SC_NOT_FOUND, 
                                      "Subscription not found");
                return;
            }
            
            // Update fields if provided
            if (requestBody.containsKey("name")) {
                String name = (String) requestBody.get("name");
                if (name != null && !name.trim().isEmpty()) {
                    subscription.setName(name.trim());
                }
            }
            
            if (requestBody.containsKey("amount")) {
                Object amountObj = requestBody.get("amount");
                try {
                    double amount = 0;
                    if (amountObj instanceof Number) {
                        amount = ((Number) amountObj).doubleValue();
                    } else if (amountObj instanceof String) {
                        amount = Double.parseDouble((String) amountObj);
                    }
                    
                    if (amount > 0) {
                        subscription.setAmount(amount);
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid amount, keep existing
                }
            }
            
            boolean dueDayChanged = false;
            if (requestBody.containsKey("due_day")) {
                Object dueDayObj = requestBody.get("due_day");
                try {
                    int dueDay = 0;
                    if (dueDayObj instanceof Number) {
                        dueDay = ((Number) dueDayObj).intValue();
                    } else if (dueDayObj instanceof String) {
                        dueDay = Integer.parseInt((String) dueDayObj);
                    }
                    
                    if (dueDay >= 1 && dueDay <= 31) {
                        subscription.setDueDay(dueDay);
                        dueDayChanged = true;
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid due day, keep existing
                }
            }
            
            // Recalculate due date if due day changed
            if (dueDayChanged) {
                Date newDueDate = dateCalculator.calculateNextDueDate(subscription.getDueDay());
                subscription.setNextDueDate(newDueDate);
            }
            
            boolean updated = subscriptionDAO.update(subscription);
            
            if (updated) {
                JsonResponse.sendSuccess(response, "Subscription updated successfully", subscription);
            } else {
                JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                      "Failed to update subscription");
            }
            
        } catch (NumberFormatException e) {
            JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                  "Invalid subscription ID");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating subscription", e);
            JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                  "Error updating subscription");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                  "Invalid request format");
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        int userId = (int) session.getAttribute("userId");
        
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Subscription ID required");
                return;
            }
            
            String[] parts = pathInfo.split("/");
            if (parts.length < 2) {
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Invalid URL format");
                return;
            }
            
            int subscriptionId = Integer.parseInt(parts[1]);
            
            boolean deleted = subscriptionDAO.delete(subscriptionId, userId);
            
            if (deleted) {
                JsonResponse.sendSuccess(response, "Subscription deleted successfully", null);
            } else {
                JsonResponse.sendError(response, HttpServletResponse.SC_NOT_FOUND, 
                                      "Subscription not found");
            }
            
        } catch (NumberFormatException e) {
            JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                  "Invalid subscription ID");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting subscription", e);
            JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                  "Error deleting subscription");
        }
    }
}
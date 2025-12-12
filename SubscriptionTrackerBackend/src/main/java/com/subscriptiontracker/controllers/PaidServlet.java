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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/subscriptions/*/paid")
public class PaidServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(PaidServlet.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
    private final DateCalculator dateCalculator = new DateCalculator();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
            
            // Extract subscription ID from path like "/123/paid"
            String[] parts = pathInfo.split("/");
            if (parts.length < 2) {
                JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                      "Invalid URL format");
                return;
            }
            
            int subscriptionId = Integer.parseInt(parts[1]);
            
            // Get subscription
            Subscription subscription = subscriptionDAO.findById(subscriptionId, userId);
            if (subscription == null) {
                JsonResponse.sendError(response, HttpServletResponse.SC_NOT_FOUND, 
                                      "Subscription not found");
                return;
            }
            
            // Calculate new due date (one month forward)
            Date currentDueDate = subscription.getNextDueDate();
            Date newDueDate = dateCalculator.markAsPaid(currentDueDate);
            
            // Update in database
            boolean updated = subscriptionDAO.updateNextDueDate(
                subscriptionId, userId, newDueDate);
            
            if (updated) {
                subscription.setNextDueDate(newDueDate);
                JsonResponse.sendSuccess(response, "Subscription marked as paid", subscription);
            } else {
                JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                      "Failed to mark subscription as paid");
            }
            
        } catch (NumberFormatException e) {
            JsonResponse.sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                                  "Invalid subscription ID");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking subscription as paid", e);
            JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                  "Error updating subscription");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            JsonResponse.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                                  "Server error occurred");
        }
    }
}
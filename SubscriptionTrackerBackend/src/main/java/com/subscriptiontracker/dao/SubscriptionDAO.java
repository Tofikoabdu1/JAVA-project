package com.subscriptiontracker.dao;

import com.subscriptiontracker.config.DatabaseConnection;
import com.subscriptiontracker.models.Subscription;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubscriptionDAO {
    private static final Logger LOGGER = Logger.getLogger(SubscriptionDAO.class.getName());
    
    public Subscription create(Subscription subscription) throws SQLException {
        String sql = "INSERT INTO subscriptions (user_id, name, amount, due_day, next_due_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, subscription.getUserId());
            pstmt.setString(2, subscription.getName());
            pstmt.setDouble(3, subscription.getAmount());
            pstmt.setInt(4, subscription.getDueDay());
            pstmt.setDate(5, subscription.getNextDueDate());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        subscription.setId(rs.getInt(1));
                    }
                }
            }
            return subscription;
        }
    }
    
    public List<Subscription> findByUserId(int userId) throws SQLException {
        List<Subscription> subscriptions = new ArrayList<>();
        String sql = "SELECT * FROM subscriptions WHERE user_id = ? ORDER BY next_due_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    subscriptions.add(mapResultSetToSubscription(rs));
                }
            }
        }
        return subscriptions;
    }
    
    public Subscription findById(int id, int userId) throws SQLException {
        String sql = "SELECT * FROM subscriptions WHERE id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.setInt(2, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSubscription(rs);
                }
            }
        }
        return null;
    }
    
    public boolean update(Subscription subscription) throws SQLException {
        String sql = "UPDATE subscriptions SET name = ?, amount = ?, due_day = ?, next_due_date = ? WHERE id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subscription.getName());
            pstmt.setDouble(2, subscription.getAmount());
            pstmt.setInt(3, subscription.getDueDay());
            pstmt.setDate(4, subscription.getNextDueDate());
            pstmt.setInt(5, subscription.getId());
            pstmt.setInt(6, subscription.getUserId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean delete(int id, int userId) throws SQLException {
        String sql = "DELETE FROM subscriptions WHERE id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateNextDueDate(int id, int userId, Date nextDueDate) throws SQLException {
        String sql = "UPDATE subscriptions SET next_due_date = ? WHERE id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, nextDueDate);
            pstmt.setInt(2, id);
            pstmt.setInt(3, userId);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    private Subscription mapResultSetToSubscription(ResultSet rs) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setId(rs.getInt("id"));
        subscription.setUserId(rs.getInt("user_id"));
        subscription.setName(rs.getString("name"));
        subscription.setAmount(rs.getDouble("amount"));
        subscription.setDueDay(rs.getInt("due_day"));
        subscription.setNextDueDate(rs.getDate("next_due_date"));
        subscription.setCreatedAt(rs.getTimestamp("created_at"));
        subscription.setUpdatedAt(rs.getTimestamp("updated_at"));
        return subscription;
    }
}
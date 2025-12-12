package com.subscriptiontracker.models;

import java.sql.Date;
import java.sql.Timestamp;

public class Subscription {
    private int id;
    private int userId;
    private String name;
    private double amount;
    private int dueDay;
    private Date nextDueDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public Subscription() {}
    
    public Subscription(int userId, String name, double amount, int dueDay) {
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.dueDay = dueDay;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public int getDueDay() { return dueDay; }
    public void setDueDay(int dueDay) { this.dueDay = dueDay; }
    
    public Date getNextDueDate() { return nextDueDate; }
    public void setNextDueDate(Date nextDueDate) { this.nextDueDate = nextDueDate; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
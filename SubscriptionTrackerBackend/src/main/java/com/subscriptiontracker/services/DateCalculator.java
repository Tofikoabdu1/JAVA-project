package com.subscriptiontracker.services;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public class DateCalculator {
    
    public Date calculateNextDueDate(int dueDay) {
        LocalDate today = LocalDate.now();
        LocalDate nextDueDate;
        
        // Calculate next due date
        if (dueDay >= today.getDayOfMonth()) {
            // Due this month
            nextDueDate = LocalDate.of(today.getYear(), today.getMonthValue(), 
                                      adjustDayForMonth(dueDay, today.getYear(), today.getMonthValue()));
        } else {
            // Due next month
            LocalDate nextMonth = today.plusMonths(1);
            nextDueDate = LocalDate.of(nextMonth.getYear(), nextMonth.getMonthValue(), 
                                      adjustDayForMonth(dueDay, nextMonth.getYear(), nextMonth.getMonthValue()));
        }
        
        return Date.valueOf(nextDueDate);
    }
    
    public Date markAsPaid(Date currentDueDate) {
        LocalDate dueDate = currentDueDate.toLocalDate();
        LocalDate newDueDate = dueDate.plusMonths(1);
        
        // Adjust if the new month doesn't have the same day
        int originalDay = dueDate.getDayOfMonth();
        newDueDate = LocalDate.of(newDueDate.getYear(), newDueDate.getMonthValue(),
                                 adjustDayForMonth(originalDay, newDueDate.getYear(), newDueDate.getMonthValue()));
        
        return Date.valueOf(newDueDate);
    }
    
    private int adjustDayForMonth(int desiredDay, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int lastDayOfMonth = yearMonth.lengthOfMonth();
        
        if (desiredDay > lastDayOfMonth) {
            return lastDayOfMonth;
        }
        return desiredDay;
    }
    
    public long calculateDaysRemaining(Date dueDate) {
        LocalDate today = LocalDate.now();
        LocalDate due = dueDate.toLocalDate();
        
        return ChronoUnit.DAYS.between(today, due);
    }
}
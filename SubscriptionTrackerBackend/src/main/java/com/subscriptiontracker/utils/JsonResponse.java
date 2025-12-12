package com.subscriptiontracker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonResponse {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static void sendJsonResponse(HttpServletResponse response, int statusCode, 
                                       String message, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("status", statusCode);
        jsonResponse.put("message", message);
        jsonResponse.put("data", data);
        
        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(jsonResponse));
        out.flush();
    }
    
    public static void sendError(HttpServletResponse response, int statusCode, 
                                String message) throws IOException {
        sendJsonResponse(response, statusCode, message, null);
    }
    
    public static void sendSuccess(HttpServletResponse response, String message, 
                                  Object data) throws IOException {
        sendJsonResponse(response, HttpServletResponse.SC_OK, message, data);
    }
}
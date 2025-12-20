package com.subscriptiontracker.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CorsFilter implements Filter {
    
    // Define allowed origins
    private static final String[] ALLOWED_ORIGINS = {
        "http://localhost:5173",  // Vite dev server
        "http://localhost:3000",  // React dev server
        "http://127.0.0.1:5173"   // Alternative localhost
    };
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Get the origin from request
        String origin = httpRequest.getHeader("Origin");
        
        // Check if origin is allowed
        String allowedOrigin = checkOrigin(origin);
        
        // Set CORS headers with specific allowed origin
        httpResponse.setHeader("Access-Control-Allow-Origin", allowedOrigin);
        httpResponse.setHeader("Access-Control-Allow-Methods", 
            "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", 
            "Origin, Content-Type, Accept, Authorization, X-Requested-With");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        
        // Handle preflight OPTIONS request
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return; // Don't continue the chain for OPTIONS
        }
        
        // Pass request down the filter chain
        chain.doFilter(request, response);
    }
    
    private String checkOrigin(String origin) {
        if (origin == null) {
            return ""; // No origin header, return empty
        }
        
        // Check if origin is in allowed list
        for (String allowed : ALLOWED_ORIGINS) {
            if (allowed.equals(origin)) {
                return origin; // Return the specific origin
            }
        }
        
        return ""; // Origin not allowed, return empty
    }
    
    @Override
    public void destroy() {
        // Cleanup code
    }
}
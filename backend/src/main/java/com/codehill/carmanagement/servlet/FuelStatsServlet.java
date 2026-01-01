package com.codehill.carmanagement.servlet;

import com.codehill.carmanagement.dto.FuelStatisticsResponse;
import com.codehill.carmanagement.exception.CarNotFoundException;
import com.codehill.carmanagement.exception.ValidationException;
import com.codehill.carmanagement.service.FuelStatisticsService;
import com.codehill.carmanagement.util.JsonUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class FuelStatsServlet extends HttpServlet {
    
    private FuelStatisticsService statisticsService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.statisticsService = FuelStatisticsService.getInstance();
        System.out.println("FuelStatsServlet initialized successfully");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
    
            Long carId = extractCarIdFromPath(request.getPathInfo());
            
            FuelStatisticsResponse stats = statisticsService.calculateStatistics(carId);
            
            response.setStatus(HttpServletResponse.SC_OK);

            String jsonResponse = JsonUtil.toJson(stats);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            
        } catch (CarNotFoundException e) {
            handleNotFoundError(response, e);
            
        } catch (ValidationException e) {
            handleValidationError(response, e);
            
        } catch (Exception e) {
            handleInternalError(response, e);
        }
    }
    
    private Long extractCarIdFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.isEmpty()) {
            throw new ValidationException("Car ID is required in the URL path");
        }
        
        String[] parts = pathInfo.split("/");
        
        if (parts.length < 2) {
            throw new ValidationException("Invalid URL format. Expected: /api/cars/{carId}/fuel/stats");
        }
        
        try {
            Long carId = Long.parseLong(parts[1]);
            
            if (carId <= 0) {
                throw new ValidationException("Car ID must be a positive number");
            }
            
            return carId;
            
        } catch (NumberFormatException e) {
            throw new ValidationException("Car ID must be a valid number");
        }
    }
    
    private void handleNotFoundError(HttpServletResponse response, CarNotFoundException e) 
            throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        String errorJson = String.format("{\"error\": \"Not found\", \"message\": \"%s\"}", 
                                        e.getMessage());
        PrintWriter out = response.getWriter();
        out.print(errorJson);
        out.flush();
    }
    
    private void handleValidationError(HttpServletResponse response, ValidationException e) 
            throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String errorJson = String.format("{\"error\": \"Validation failed\", \"message\": \"%s\"}", 
                                        e.getMessage());
        PrintWriter out = response.getWriter();
        out.print(errorJson);
        out.flush();
    }
    
    private void handleInternalError(HttpServletResponse response, Exception e) 
            throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String errorJson = String.format("{\"error\": \"Internal server error\", \"message\": \"%s\"}", 
                                        e.getMessage());
        PrintWriter out = response.getWriter();
        out.print(errorJson);
        out.flush();
        e.printStackTrace();
    }
    
    @Override
    public void destroy() {
        System.out.println("FuelStatsServlet destroyed");
        super.destroy();
    }
}

package com.codehill.carmanagement.servlet;

import com.codehill.carmanagement.dto.FuelStatisticsResponse;
import com.codehill.carmanagement.exception.CarNotFoundException;
import com.codehill.carmanagement.service.FuelStatisticsService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


public class FuelStatsQueryServlet extends HttpServlet {
    
    private FuelStatisticsService statisticsService;
    
  
    @Override
    public void init() throws ServletException {
        super.init();
        
        this.statisticsService = FuelStatisticsService.getInstance();
        
        System.out.println("[LIFECYCLE] FuelStatsQueryServlet.init() called - Servlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        System.out.println("[LIFECYCLE] FuelStatsQueryServlet.doGet() called - Processing request");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String carIdParam = request.getParameter("carId");
            
            if (carIdParam == null || carIdParam.trim().isEmpty()) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                
                String errorJson = "{\"error\": \"Bad request\", " +
                                 "\"message\": \"carId query parameter is required\"}";
                
                PrintWriter out = response.getWriter();
                out.print(errorJson);
                out.flush();
                return;
            }

            Long carId;
            try {
                carId = Long.parseLong(carIdParam);
                
                if (carId <= 0) {
                    throw new NumberFormatException("Car ID must be positive");
                }
                
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                
                String errorJson = String.format(
                    "{\"error\": \"Bad request\", " +
                    "\"message\": \"carId must be a valid positive number, got: %s\"}", 
                    carIdParam
                );
                
                PrintWriter out = response.getWriter();
                out.print(errorJson);
                out.flush();
                return;
            }
            
            FuelStatisticsResponse stats = statisticsService.calculateStatistics(carId);
            
            response.setStatus(HttpServletResponse.SC_OK);
            
            String jsonResponse = buildJsonResponse(stats);
            
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            
            System.out.println("[LIFECYCLE] FuelStatsQueryServlet.doGet() completed successfully");
            
        } catch (CarNotFoundException e) {
            System.out.println("[LIFECYCLE] FuelStatsQueryServlet.doGet() - Car not found");
            
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            
            String errorJson = String.format(
                "{\"error\": \"Not found\", \"message\": \"%s\"}", 
                e.getMessage()
            );
            
            PrintWriter out = response.getWriter();
            out.print(errorJson);
            out.flush();
            
        } catch (Exception e) {
            System.out.println("[LIFECYCLE] FuelStatsQueryServlet.doGet() - Internal error");
            e.printStackTrace();
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            
            String errorJson = String.format(
                "{\"error\": \"Internal server error\", \"message\": \"%s\"}", 
                e.getMessage()
            );
            
            PrintWriter out = response.getWriter();
            out.print(errorJson);
            out.flush();
        }
    }
    
    private String buildJsonResponse(FuelStatisticsResponse stats) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append(String.format("  \"totalFuel\": %.2f,\n", stats.getTotalFuel()));
        json.append(String.format("  \"totalCost\": %.2f", stats.getTotalCost()));
        
        if (stats.getAverageConsumption() != null) {
            json.append(",\n");
            json.append(String.format("  \"averageConsumption\": %.2f\n", stats.getAverageConsumption()));
        } else {
            json.append(",\n");
            json.append("  \"averageConsumption\": null\n");
        }
        
        json.append("}");
        return json.toString();
    }
    
    @Override
    public void destroy() {
        System.out.println("[LIFECYCLE] FuelStatsQueryServlet.destroy() called - Servlet being destroyed");
        
        this.statisticsService = null;
        
        super.destroy();
    }
    
    @Override
    public String getServletInfo() {
        return "FuelStatsQueryServlet - Demonstrates servlet lifecycle and manual HTTP handling";
    }
}

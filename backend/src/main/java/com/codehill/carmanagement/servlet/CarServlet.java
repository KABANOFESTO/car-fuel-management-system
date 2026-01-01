package com.codehill.carmanagement.servlet;

import com.codehill.carmanagement.dto.CarRequest;
import com.codehill.carmanagement.exception.ValidationException;
import com.codehill.carmanagement.model.Car;
import com.codehill.carmanagement.service.CarService;
import com.codehill.carmanagement.util.JsonUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class CarServlet extends HttpServlet {
    
    private CarService carService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.carService = CarService.getInstance();
        
        System.out.println("CarServlet initialized successfully");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
        
            CarRequest carRequest = JsonUtil.readRequestBody(request, CarRequest.class);
            
            Car createdCar = carService.createCar(carRequest);
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            
            String jsonResponse = JsonUtil.toJson(createdCar);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            
        } catch (ValidationException e) {
            handleValidationError(response, e);
            
        } catch (Exception e) {
            handleInternalError(response, e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            List<Car> cars = carService.getAllCars();
            
            response.setStatus(HttpServletResponse.SC_OK);
            
            String jsonResponse = JsonUtil.toJson(cars);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
            
        } catch (Exception e) {
            handleInternalError(response, e);
        }
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
        System.out.println("CarServlet destroyed");
        super.destroy();
    }
}

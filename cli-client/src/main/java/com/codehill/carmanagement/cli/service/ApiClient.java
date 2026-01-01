package com.codehill.carmanagement.cli.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiClient {
    

    private static volatile ApiClient instance;
    
    private final String baseUrl;

    private final HttpClient httpClient;
    

    private final ObjectMapper objectMapper;
    

    private ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    public static ApiClient getInstance() {
        String envUrl = System.getenv("API_URL");
        return getInstance(envUrl != null && !envUrl.isEmpty() ? envUrl : "http://localhost:8080");
    }
    
    public static ApiClient getInstance(String baseUrl) {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient(baseUrl);
                }
            }
        }
        return instance;
    }
    

    public Map<String, Object> createCar(String brand, String model, int year) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("brand", brand);
        requestBody.put("model", model);
        requestBody.put("year", year);
        
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cars"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return handleResponse(response);
    }
    
    public Map<String, Object> addFuelEntry(long carId, double liters, double price, int odometer) 
            throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("liters", liters);
        requestBody.put("price", price);
        requestBody.put("odometer", odometer);
        
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/fuel/cars/" + carId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return handleResponse(response);
    }
    
    public Map<String, Object> getFuelStatistics(long carId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/fuel/cars/" + carId + "/stats"))
                .header("Accept", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return handleResponse(response);
    }
    
    public List<Map<String, Object>> getAllCars() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cars"))
                .header("Accept", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return handleListResponse(response);
    }
    
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> handleListResponse(HttpResponse<String> response) throws Exception {
        int statusCode = response.statusCode();
        String body = response.body();
        
        if (statusCode >= 200 && statusCode < 300) {
            return objectMapper.readValue(body, List.class);
        } else {
            // Re-use the error handling logic from handleResponse by creating a temporary Map response or just parsing here
            // Since handleResponse is private and returns Map, we can't easily reuse it exactly without refactoring.
            // But we can duplicate the error parsing logic or extracting it.
            // For simplicity, let's parse the error body as Map
            Map<String, Object> errorMap = new HashMap<>();
            if (body != null && !body.trim().isEmpty()) {
                try {
                    errorMap = objectMapper.readValue(body, Map.class);
                } catch (Exception e) {
                   // ignore
                }
            }
            
            if (statusCode == 404) {
                 String message = (String) errorMap.getOrDefault("message", "Resource not found (404)");
                 throw new Exception(message);
            } else if (statusCode == 400) {
                 String message = (String) errorMap.getOrDefault("message", "Invalid request (400)");
                 throw new Exception(message);
            } else if (statusCode >= 500) {
                 String message = (String) errorMap.getOrDefault("message", "Server error (500)");
                 throw new Exception(message);
            } else {
                 String message = (String) errorMap.getOrDefault("message", "HTTP error " + statusCode);
                 if (errorMap.isEmpty() && body != null && !body.isEmpty()) {
                      message += ": " + body;
                 }
                 throw new Exception(message);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> handleResponse(HttpResponse<String> response) throws Exception {
        int statusCode = response.statusCode();
        String body = response.body();
        
        Map<String, Object> responseMap = new HashMap<>();
        if (body != null && !body.trim().isEmpty()) {
            try {
                responseMap = objectMapper.readValue(body, Map.class);
            } catch (Exception e) {
                if (statusCode >= 200 && statusCode < 300) {
                    throw new Exception("Failed to parse success response: " + e.getMessage());
                }
            }
        }
        
        if (statusCode >= 200 && statusCode < 300) {
            return responseMap;
            
        } else if (statusCode == 404) {
            String message = (String) responseMap.getOrDefault("message", "Resource not found (404)");
            throw new Exception(message);
            
        } else if (statusCode == 400) {
            String message = (String) responseMap.getOrDefault("message", "Invalid request (400)");
            throw new Exception(message);
            
        } else if (statusCode >= 500) {
            String message = (String) responseMap.getOrDefault("message", "Server error (500)");
            throw new Exception(message);
            
        } else {
            String message = (String) responseMap.getOrDefault("message", "HTTP error " + statusCode);
            if (responseMap.isEmpty() && body != null && !body.isEmpty()) {
                 message += ": " + body;
            }
            throw new Exception(message);
        }
    }
}

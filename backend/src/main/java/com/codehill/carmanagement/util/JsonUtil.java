package com.codehill.carmanagement.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;


public class JsonUtil {
    
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();
    
    private JsonUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.registerModule(new JavaTimeModule());
        
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        return mapper;
    }
    
    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }
    
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON: " + e.getMessage(), e);
        }
    }
    

    public static <T> T readRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        
        String json = jsonBuilder.toString();
        if (json.isEmpty()) {
            throw new RuntimeException("Request body is empty");
        }
        
        return fromJson(json, clazz);
    }
    
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}

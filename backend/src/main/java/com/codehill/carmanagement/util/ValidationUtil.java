package com.codehill.carmanagement.util;

import com.codehill.carmanagement.dto.CarRequest;
import com.codehill.carmanagement.dto.FuelEntryRequest;
import com.codehill.carmanagement.exception.ValidationException;


public class ValidationUtil {
    
    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    public static void validateCarRequest(CarRequest request) {
        if (request == null) {
            throw new ValidationException("Car request cannot be null");
        }
        
        validateNotEmpty(request.getBrand(), "Brand");
        validateNotEmpty(request.getModel(), "Model");
        validateYearRange(request.getYear());
    }
    
    public static void validateFuelEntryRequest(FuelEntryRequest request) {
        if (request == null) {
            throw new ValidationException("Fuel entry request cannot be null");
        }
        
        validatePositive(request.getLiters(), "Liters");
        validatePositive(request.getPrice(), "Price");
        validateNonNegative(request.getOdometer(), "Odometer");
    }
    
 
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be null or empty");
        }
    }
    

    public static void validatePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " must be positive (greater than 0)");
        }
    }
    
    public static void validateNonNegative(int value, String fieldName) {
        if (value < 0) {
            throw new ValidationException(fieldName + " cannot be negative");
        }
    }
    
    public static void validateYearRange(int year) {
        if (year < 1900 || year > 2100) {
            throw new ValidationException("Year must be between 1900 and 2100");
        }
    }
    
    public static void validateCarId(Long carId) {
        if (carId == null || carId <= 0) {
            throw new ValidationException("Car ID must be a positive number");
        }
    }
}

package com.codehill.carmanagement.exception;


public class CarNotFoundException extends RuntimeException {
    
    private final Long carId;
    
    public CarNotFoundException(Long carId) {
        super(String.format("Car with ID %d not found", carId));
        this.carId = carId;
    }
    
    public Long getCarId() {
        return carId;
    }
}

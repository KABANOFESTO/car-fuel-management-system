package com.codehill.carmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelEntry {
    
    private Long id;
    
    private Long carId;
    
    private double liters;
    
    private double price;
    
    private int odometer;
    
    private LocalDateTime timestamp;
    
    @Override
    public String toString() {
        return String.format("FuelEntry[id=%d, carId=%d, liters=%.2f, price=%.2f, odometer=%d, timestamp=%s]",
                           id, carId, liters, price, odometer, timestamp);
    }
}

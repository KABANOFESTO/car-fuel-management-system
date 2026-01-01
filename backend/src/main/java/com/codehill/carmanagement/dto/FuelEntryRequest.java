package com.codehill.carmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelEntryRequest {
    
    private double liters;
    
    private double price;
    
    private int odometer;
}

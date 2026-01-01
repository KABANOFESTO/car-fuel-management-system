package com.codehill.carmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelStatisticsResponse {
    
    private double totalFuel;
    
    private double totalCost;
    
    private Double averageConsumption;
    
    @Override
    public String toString() {
        String consumption = averageConsumption != null 
            ? String.format("%.2f L/100km", averageConsumption)
            : "N/A";
        
        return String.format("Total fuel: %.2f L, Total cost: %.2f, Average consumption: %s",
                           totalFuel, totalCost, consumption);
    }
}

package com.codehill.carmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    

    private String brand;
    
    private String model;
    
    private int year;
}

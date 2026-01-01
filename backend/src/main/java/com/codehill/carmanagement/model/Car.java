package com.codehill.carmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    
  
    private Long id;
    
    private String brand;
    
    private String model;
    
    private int year;
    
    @Override
    public String toString() {
        return String.format("Car[id=%d, brand='%s', model='%s', year=%d]", 
                           id, brand, model, year);
    }
}

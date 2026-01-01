package com.codehill.carmanagement.service;

import com.codehill.carmanagement.dto.FuelStatisticsResponse;
import com.codehill.carmanagement.exception.CarNotFoundException;
import com.codehill.carmanagement.model.FuelEntry;
import com.codehill.carmanagement.repository.CarRepository;
import com.codehill.carmanagement.repository.FuelEntryRepository;

import java.util.List;


public class FuelStatisticsService {
    
    private static volatile FuelStatisticsService instance;
    
    private final CarRepository carRepository;
    
    private final FuelEntryRepository fuelEntryRepository;
    
    private FuelStatisticsService() {
        this.carRepository = CarRepository.getInstance();
        this.fuelEntryRepository = FuelEntryRepository.getInstance();
    }
    
    public static FuelStatisticsService getInstance() {
        if (instance == null) {
            synchronized (FuelStatisticsService.class) {
                if (instance == null) {
                    instance = new FuelStatisticsService();
                }
            }
        }
        return instance;
    }
    

    public FuelStatisticsResponse calculateStatistics(Long carId) {
        if (!carRepository.findById(carId).isPresent()) {
            throw new CarNotFoundException(carId);
        }
        
        List<FuelEntry> entries = fuelEntryRepository.findByCarId(carId);
        
        if (entries.isEmpty()) {
            return FuelStatisticsResponse.builder()
                    .totalFuel(0.0)
                    .totalCost(0.0)
                    .averageConsumption(null)
                    .build();
        }
        
        double totalFuel = entries.stream()
                .mapToDouble(FuelEntry::getLiters)
                .sum();
        
        double totalCost = entries.stream()
                .mapToDouble(FuelEntry::getPrice)
                .sum();
        
        Double averageConsumption = calculateAverageConsumption(entries, totalFuel);
        
        return FuelStatisticsResponse.builder()
                .totalFuel(Math.round(totalFuel * 100.0) / 100.0) 
                .totalCost(Math.round(totalCost * 100.0) / 100.0)
                .averageConsumption(averageConsumption)
                .build();
    }
    

    private Double calculateAverageConsumption(List<FuelEntry> entries, double totalFuel) {
        if (entries.size() < 2) {
            return null;
        }
        
        int firstOdometer = entries.get(0).getOdometer();
        int lastOdometer = entries.get(entries.size() - 1).getOdometer();
        
        int distanceTraveled = lastOdometer - firstOdometer;
        
        if (distanceTraveled == 0) {
            return null;
        }
        
        double consumption = (totalFuel / distanceTraveled) * 100;
        
        return Math.round(consumption * 100.0) / 100.0;
    }
}

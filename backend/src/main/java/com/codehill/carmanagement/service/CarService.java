package com.codehill.carmanagement.service;

import com.codehill.carmanagement.dto.CarRequest;
import com.codehill.carmanagement.dto.FuelEntryRequest;
import com.codehill.carmanagement.exception.CarNotFoundException;
import com.codehill.carmanagement.exception.ValidationException;
import com.codehill.carmanagement.model.Car;
import com.codehill.carmanagement.model.FuelEntry;
import com.codehill.carmanagement.repository.CarRepository;
import com.codehill.carmanagement.repository.FuelEntryRepository;
import com.codehill.carmanagement.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;

public class CarService {
    
    private static volatile CarService instance;
    
    private final CarRepository carRepository;
    
    private final FuelEntryRepository fuelEntryRepository;
    
    private CarService() {
        this.carRepository = CarRepository.getInstance();
        this.fuelEntryRepository = FuelEntryRepository.getInstance();
    }
    
    public static CarService getInstance() {
        if (instance == null) {
            synchronized (CarService.class) {
                if (instance == null) {
                    instance = new CarService();
                }
            }
        }
        return instance;
    }
    

    public Car createCar(CarRequest request) {
        ValidationUtil.validateCarRequest(request);

        Car car = Car.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .build();
        
        return carRepository.save(car);
    }
    
    public Car getCarById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
    }
    

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    
    public FuelEntry addFuelEntry(Long carId, FuelEntryRequest request) {
        ValidationUtil.validateFuelEntryRequest(request);
    
        getCarById(carId);
        
        validateOdometerReading(carId, request.getOdometer());
        
        FuelEntry fuelEntry = FuelEntry.builder()
                .carId(carId)
                .liters(request.getLiters())
                .price(request.getPrice())
                .odometer(request.getOdometer())
                .timestamp(LocalDateTime.now())
                .build();
        
        return fuelEntryRepository.save(fuelEntry);
    }
    
    private void validateOdometerReading(Long carId, int newOdometer) {
        List<FuelEntry> previousEntries = fuelEntryRepository.findByCarId(carId);
        
        if (!previousEntries.isEmpty()) {
            // Get the most recent entry (list is sorted by timestamp)
            FuelEntry lastEntry = previousEntries.get(previousEntries.size() - 1);
            
            if (newOdometer < lastEntry.getOdometer()) {
                throw new ValidationException(
                    String.format("Odometer reading (%d) cannot be less than previous reading (%d)",
                                newOdometer, lastEntry.getOdometer())
                );
            }
        }
    }
    
    public List<FuelEntry> getFuelEntriesByCarId(Long carId) {
        getCarById(carId);
        return fuelEntryRepository.findByCarId(carId);
    }
}

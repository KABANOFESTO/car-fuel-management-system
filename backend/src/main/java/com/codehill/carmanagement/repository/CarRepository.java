package com.codehill.carmanagement.repository;

import com.codehill.carmanagement.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CarRepository {
    
    private static volatile CarRepository instance;
    
    private final ConcurrentHashMap<Long, Car> cars;
    
    private final AtomicLong idGenerator;
    
    private CarRepository() {
        this.cars = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(0);
    }
    
    public static CarRepository getInstance() {
        if (instance == null) {
            synchronized (CarRepository.class) {
                if (instance == null) {
                    instance = new CarRepository();
                }
            }
        }
        return instance;
    }
    
    public Car save(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        
        if (car.getId() == null) {
            car.setId(idGenerator.incrementAndGet());
        }
        
        cars.put(car.getId(), car);
        return car;
    }
    

    public Optional<Car> findById(Long id) {
        return Optional.ofNullable(cars.get(id));
    }
    
    public List<Car> findAll() {
        return new ArrayList<>(cars.values());
    }
  
    public boolean deleteById(Long id) {
        return cars.remove(id) != null;
    }
  
    public int count() {
        return cars.size();
    }
    
    public void clear() {
        cars.clear();
    }
}

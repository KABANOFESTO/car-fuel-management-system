package com.codehill.carmanagement.repository;

import com.codehill.carmanagement.model.FuelEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


public class FuelEntryRepository {
    
    private static volatile FuelEntryRepository instance;
    
    private final ConcurrentHashMap<Long, FuelEntry> fuelEntries;
    
    private final AtomicLong idGenerator;
    
    private FuelEntryRepository() {
        this.fuelEntries = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(0);
    }
    
    public static FuelEntryRepository getInstance() {
        if (instance == null) {
            synchronized (FuelEntryRepository.class) {
                if (instance == null) {
                    instance = new FuelEntryRepository();
                }
            }
        }
        return instance;
    }
    

    public FuelEntry save(FuelEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("FuelEntry cannot be null");
        }
        
        if (entry.getId() == null) {
            entry.setId(idGenerator.incrementAndGet());
        }
        
        fuelEntries.put(entry.getId(), entry);
        return entry;
    }
    
    public Optional<FuelEntry> findById(Long id) {
        return Optional.ofNullable(fuelEntries.get(id));
    }
    
    public List<FuelEntry> findByCarId(Long carId) {
        return fuelEntries.values().stream()
                .filter(entry -> entry.getCarId().equals(carId))
                .sorted(Comparator.comparing(FuelEntry::getTimestamp))
                .collect(Collectors.toList());
    }
    
    public List<FuelEntry> findAll() {
        return new ArrayList<>(fuelEntries.values());
    }
    
    public boolean deleteById(Long id) {
        return fuelEntries.remove(id) != null;
    }
    
    public int count() {
        return fuelEntries.size();
    }
    
    public void clear() {
        fuelEntries.clear();
    }
}

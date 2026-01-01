package com.codehill.carmanagement.cli;

import com.codehill.carmanagement.cli.command.CommandParser;
import com.codehill.carmanagement.cli.service.ApiClient;
import com.codehill.carmanagement.cli.util.ConsoleFormatter;

import java.util.Map;


public class CarManagementCLI {

    private final ApiClient apiClient;

    public CarManagementCLI() {
        this.apiClient = ApiClient.getInstance();
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            ConsoleFormatter.printError("No command specified");
            ConsoleFormatter.printHelp();
            System.exit(1);
        }
        
        CarManagementCLI cli = new CarManagementCLI();
        
        try {
            cli.executeCommand(args);
            System.exit(0);
            
        } catch (Exception e) {
            ConsoleFormatter.printError(e.getMessage());
            System.exit(1);
        }
    }
    
    private void executeCommand(String[] args) throws Exception {
        String command = args[0].toLowerCase();
        
        String[] paramArgs = new String[args.length - 1];
        System.arraycopy(args, 1, paramArgs, 0, args.length - 1);
        
        Map<String, String> params = CommandParser.parseArguments(paramArgs);
        
        switch (command) {
            case "create-car":
                executeCreateCar(params);
                break;
                
            case "list-cars":
                executeListCars();
                break;
                
            case "add-fuel":
                executeAddFuel(params);
                break;
                
            case "fuel-stats":
                executeFuelStats(params);
                break;
                
            case "help":
            case "--help":
            case "-h":
                ConsoleFormatter.printHelp();
                break;
                
            default:
                throw new IllegalArgumentException("Unknown command: " + command + ". Use 'help' to see available commands.");
        }
    }
    
    private void executeListCars() throws Exception {
        ConsoleFormatter.printInfo("Retrieving all cars...");
        java.util.List<java.util.Map<String, Object>> cars = apiClient.getAllCars();
        ConsoleFormatter.printCars(cars);
    }

    private void executeCreateCar(Map<String, String> params) throws Exception {

        String brand = CommandParser.getRequiredParam(params, "brand");
        String model = CommandParser.getRequiredParam(params, "model");
        int year = CommandParser.getRequiredIntParam(params, "year");
        
        ConsoleFormatter.printInfo("Creating car: " + brand + " " + model + " (" + year + ")...");
        Map<String, Object> response = apiClient.createCar(brand, model, year);
        
        Object carId = response.get("id");
        ConsoleFormatter.printSuccess("Car created successfully! ID: " + carId);
        System.out.println();
        System.out.println("  Brand:  " + response.get("brand"));
        System.out.println("  Model:  " + response.get("model"));
        System.out.println("  Year:   " + response.get("year"));
        System.out.println();
    }

    private void executeAddFuel(Map<String, String> params) throws Exception {

        long carId = CommandParser.getRequiredLongParam(params, "carId");
        double liters = CommandParser.getRequiredDoubleParam(params, "liters");
        double price = CommandParser.getRequiredDoubleParam(params, "price");
        int odometer = CommandParser.getRequiredIntParam(params, "odometer");
        
        ConsoleFormatter.printInfo(String.format("Adding fuel entry for car #%d...", carId));
        Map<String, Object> response = apiClient.addFuelEntry(carId, liters, price, odometer);
        
        ConsoleFormatter.printSuccess("Fuel entry added successfully!");
        System.out.println();
        System.out.println("  Entry ID:  " + response.get("id"));
        System.out.println("  Liters:    " + response.get("liters") + " L");
        System.out.println("  Price:     " + response.get("price"));
        System.out.println("  Odometer:  " + response.get("odometer") + " km");
        System.out.println();
    }
    
    private void executeFuelStats(Map<String, String> params) throws Exception {
        long carId = CommandParser.getRequiredLongParam(params, "carId");
        
        ConsoleFormatter.printInfo(String.format("Retrieving fuel statistics for car #%d...", carId));
        Map<String, Object> response = apiClient.getFuelStatistics(carId);
        
        double totalFuel = ((Number) response.get("totalFuel")).doubleValue();
        double totalCost = ((Number) response.get("totalCost")).doubleValue();
        
        Double averageConsumption = null;
        Object avgObj = response.get("averageConsumption");
        if (avgObj != null) {
            averageConsumption = ((Number) avgObj).doubleValue();
        }
        
        ConsoleFormatter.printFuelStatistics(totalFuel, totalCost, averageConsumption);
    }
}

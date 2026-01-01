package com.codehill.carmanagement.cli.util;


public class ConsoleFormatter {
    
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    

    private static final String CHECK_MARK = "✓";
    private static final String CROSS_MARK = "✗";
    private static final String INFO_MARK = "ℹ";
    
    private ConsoleFormatter() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    public static void printSuccess(String message) {
        System.out.println(GREEN + CHECK_MARK + " " + message + RESET);
    }
    

    public static void printError(String message) {
        System.err.println(RED + CROSS_MARK + " " + message + RESET);
    }
    
    public static void printInfo(String message) {
        System.out.println(BLUE + INFO_MARK + " " + message + RESET);
    }
    
 
    public static void printWarning(String message) {
        System.out.println(YELLOW + "⚠ " + message + RESET);
    }
    

    public static void printHeader(String title) {
        int length = title.length() + 4;
        String border = "═".repeat(length);
        
        System.out.println(CYAN + border + RESET);
        System.out.println(CYAN + "  " + title + RESET);
        System.out.println(CYAN + border + RESET);
    }
    
 
    public static void printFuelStatistics(double totalFuel, double totalCost, Double averageConsumption) {
        System.out.println();
        printHeader("Fuel Statistics");
        System.out.println();
        
        System.out.printf("  %-25s %s%.2f L%s%n", "Total fuel:", GREEN, totalFuel, RESET);
        System.out.printf("  %-25s %s%.2f%s%n", "Total cost:", GREEN, totalCost, RESET);
        
        if (averageConsumption != null) {
            System.out.printf("  %-25s %s%.2f L/100km%s%n", "Average consumption:", GREEN, averageConsumption, RESET);
        } else {
            System.out.printf("  %-25s %sN/A (insufficient data)%s%n", "Average consumption:", YELLOW, RESET);
        }
        
        System.out.println();
        System.out.println("═".repeat(35));
        System.out.println();
    }

    public static void printCars(java.util.List<java.util.Map<String, Object>> cars) {
        System.out.println();
        printHeader("All Cars");
        System.out.println();
        
        if (cars.isEmpty()) {
            printInfo("No cars found.");
            return;
        }
        
        // Header
        System.out.printf(CYAN + "  %-5s %-20s %-20s %-10s%s%n", "ID", "Brand", "Model", "Year", RESET);
        System.out.println("  " + "─".repeat(60));
        
        for (java.util.Map<String, Object> car : cars) {
            System.out.printf("  %-5s %-20s %-20s %-10s%n",
                    car.get("id"),
                    car.get("brand"),
                    car.get("model"),
                    car.get("year"));
        }
        System.out.println();
    }

    public static void printHelp() {
        System.out.println();
        printHeader("Car Management CLI - Usage");
        System.out.println();
        
        System.out.println(CYAN + "Available Commands:" + RESET);
        System.out.println();
        
        System.out.println("  " + GREEN + "create-car" + RESET + " --brand <brand> --model <model> --year <year>");
        System.out.println("      Create a new car");
        System.out.println("      Example: create-car --brand Toyota --model Corolla --year 2018");
        System.out.println();
        
        System.out.println("  " + GREEN + "list-cars" + RESET);
        System.out.println("      List all cars");
        System.out.println();
        
        System.out.println("  " + GREEN + "add-fuel" + RESET + " --carId <id> --liters <L> --price <price> --odometer <km>");
        System.out.println("      Add a fuel entry for a car");
        System.out.println("      Example: add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000");
        System.out.println();
        
        System.out.println("  " + GREEN + "fuel-stats" + RESET + " --carId <id>");
        System.out.println("      View fuel statistics for a car");
        System.out.println("      Example: fuel-stats --carId 1");
        System.out.println();
        
        System.out.println("  " + GREEN + "help" + RESET);
        System.out.println("      Display this help message");
        System.out.println();
    }
}

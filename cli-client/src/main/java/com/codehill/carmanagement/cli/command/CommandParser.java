package com.codehill.carmanagement.cli.command;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {
    
    private CommandParser() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    public static Map<String, String> parseArguments(String[] args) {
        Map<String, String> params = new HashMap<>();
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String paramName = args[i].substring(2);
                
                if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    params.put(paramName, args[i + 1]);
                    i++; 
                } else {
                    params.put(paramName, "true");
                }
            }
        }
        
        return params;
    }
    
    public static String getRequiredParam(Map<String, String> params, String name) {
        String value = params.get(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required parameter: --" + name);
        }
        return value;
    }
    
    public static int getRequiredIntParam(Map<String, String> params, String name) {
        String value = getRequiredParam(params, name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Parameter --" + name + " must be a valid integer, got: " + value);
        }
    }
    
    public static long getRequiredLongParam(Map<String, String> params, String name) {
        String value = getRequiredParam(params, name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Parameter --" + name + " must be a valid number, got: " + value);
        }
    }
    
    public static double getRequiredDoubleParam(Map<String, String> params, String name) {
        String value = getRequiredParam(params, name);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Parameter --" + name + " must be a valid number, got: " + value);
        }
    }
}

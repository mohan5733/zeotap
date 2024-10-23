package com.weather1;

public class WeatherData {
    private String mainCondition;
    private double temperature; // in Celsius
    private double feelsLike; // in Celsius
    private long timestamp;

    // Constructor
    public WeatherData(String mainCondition, double temperature, double feelsLike, long timestamp) {
        this.mainCondition = mainCondition;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.timestamp = timestamp;
    }

    // Getters and toString method for debugging
    public String getMainCondition() { return mainCondition; }
    public double getTemperature() { return temperature; }
    public double getFeelsLike() { return feelsLike; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "WeatherData{" +
                "mainCondition='" + mainCondition + '\'' +
                ", temperature=" + temperature +
                ", feelsLike=" + feelsLike +
                ", timestamp=" + timestamp +
                '}';
    }
}


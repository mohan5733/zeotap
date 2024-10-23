package com.weather1;

public class WeatherSummary {
    private double avgTemperature;
    private double maxTemperature;
    private double minTemperature;
    private String dominantCondition;

    public WeatherSummary(double avgTemperature, double maxTemperature, double minTemperature, String dominantCondition) {
        this.avgTemperature = avgTemperature;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.dominantCondition = dominantCondition;
    }

    @Override
    public String toString() {
        return "WeatherSummary{" +
                "avgTemperature=" + avgTemperature +
                ", maxTemperature=" + maxTemperature +
                ", minTemperature=" + minTemperature +
                ", dominantCondition='" + dominantCondition + '\'' +
                '}';
    }
}


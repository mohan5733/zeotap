package com.weather1;

public class AlertManager {
    private double temperatureThreshold;
    private int alertCount;

    public AlertManager(double temperatureThreshold) {
        this.temperatureThreshold = temperatureThreshold;
        this.alertCount = 0;
    }

    public void checkForAlerts(WeatherData data) {
        if (data.getTemperature() > temperatureThreshold) {
            alertCount++;
            if (alertCount >= 2) {
                sendAlert(data);
            }
        } else {
            alertCount = 0; // Reset if below threshold
        }
    }

    private void sendAlert(WeatherData data) {
        // Implement alerting mechanism (e.g., console output or email)
        System.out.println("ALERT: Temperature exceeded threshold: " + data);
        // Email notification logic can be added here.
    }
}


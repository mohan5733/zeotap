

package com.weather1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DatabaseManager {
    private String storagePath;

    public DatabaseManager(String storagePath) {
        this.storagePath = storagePath;
    }

    public void storeDailySummary(WeatherSummary summary) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storagePath, true))) {
            writer.write(summary.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}




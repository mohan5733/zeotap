package com.weather1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeatherAggregator {
    private HashMap<Long, List<WeatherData>> dailyWeatherData;

    public WeatherAggregator() {
        this.dailyWeatherData = new HashMap<>();
    }

    public void addWeatherData(WeatherData data) {
        long date = data.getTimestamp() / 86400; // Roll up to day
        dailyWeatherData.putIfAbsent(date, new ArrayList<>());
        dailyWeatherData.get(date).add(data);
    }

    public WeatherSummary calculateDailySummary(long date) {
        List<WeatherData> dataForDay = dailyWeatherData.get(date);
        if (dataForDay == null || dataForDay.isEmpty()) {
            return null;
        }

        double totalTemp = 0;
        double maxTemp = Double.MIN_VALUE;
        double minTemp = Double.MAX_VALUE;
        HashMap<String, Integer> conditionCount = new HashMap<>();

        for (WeatherData data : dataForDay) {
            totalTemp += data.getTemperature();
            maxTemp = Math.max(maxTemp, data.getTemperature());
            minTemp = Math.min(minTemp, data.getTemperature());
            conditionCount.put(data.getMainCondition(), conditionCount.getOrDefault(data.getMainCondition(), 0) + 1);
        }

        double avgTemp = totalTemp / dataForDay.size();
        String dominantCondition = conditionCount.entrySet()
                .stream()
                .max(HashMap.Entry.comparingByValue())
                .get()
                .getKey();

        return new WeatherSummary(avgTemp, maxTemp, minTemp, dominantCondition);
    }
}


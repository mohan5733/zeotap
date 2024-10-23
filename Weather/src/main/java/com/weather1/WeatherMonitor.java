package com.weather1;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherMonitor {
    private final String apiKey;
    private final String apiUrl;
    private final List<String> cities;
    private final long updateInterval;
    private final WeatherAggregator aggregator;
    private final AlertManager alertManager;
    private final DatabaseManager databaseManager;

    private static final Logger logger = LoggerFactory.getLogger(WeatherMonitor.class);

    public WeatherMonitor() throws IOException {
        Properties props = new Properties();

        // Load properties from the classpath
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Unable to find config.properties");
            }
            props.load(input);
        }

        this.apiKey = props.getProperty("api.key");
        this.apiUrl = props.getProperty("api.url");
        this.cities = List.of(props.getProperty("cities").split(","));
        this.updateInterval = Long.parseLong(props.getProperty("update.interval"));
        this.aggregator = new WeatherAggregator();
        this.alertManager = new AlertManager(35.0); // Example threshold
        this.databaseManager = new DatabaseManager("daily_weather_summaries.txt");

        startMonitoring();
    }

    private void startMonitoring() {

    	Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (String city : cities) {
                    fetchWeatherData(city);
                }
            }
        }, 0, updateInterval);
    }
    
    
    private void fetchWeatherData(String city) {
        String url = String.format("%s?q=%s&appid=%s", apiUrl, URLEncoder.encode(city, StandardCharsets.UTF_8), apiKey);
       // logger.info("Requesting weather data from URL: {}", url);
        logger.info("Fetching weather data for city: " + city);
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    processWeatherData(jsonResponse);
                } else if (statusCode == 429) {
                    logger.warn("Rate limit exceeded for city {}: Retry after some time", city);
                } else {
                    logger.error("Error fetching weather data for {}: Status code {}", city, statusCode);
                }
            }
        } catch (IOException e) {
            logger.error("IOException while fetching weather data for {}: {}", city, e.getMessage());
        }
    }


    private void processWeatherData(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            String mainCondition = json.getJSONArray("weather").getJSONObject(0).getString("main");
            double temperature = json.getJSONObject("main").getDouble("temp");
            double feelsLike = json.getJSONObject("main").getDouble("feels_like");
            long timestamp = json.getLong("dt");

            WeatherData weatherData = new WeatherData(mainCondition, temperature, feelsLike, timestamp);
            aggregator.addWeatherData(weatherData);
            alertManager.checkForAlerts(weatherData);

            long date = timestamp / 86400; // Roll up to day
            if (isEndOfDay(timestamp)) {
                WeatherSummary summary = aggregator.calculateDailySummary(date);
                if (summary != null) {
                    databaseManager.storeDailySummary(summary);
                }
            }
        } catch (Exception e) {
            logger.error("Error processing weather data: {}", e.getMessage());
        }
    }
   

    private boolean isEndOfDay(long timestamp) {
        ZonedDateTime zdt = Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault());
        return zdt.getHour() == 23 && zdt.getMinute() >= 59;
    }

    public static void main(String[] args) {
        try {
            new WeatherMonitor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

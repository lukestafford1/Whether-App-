package com.app.whether.weather;

public record WeatherDto(
        String country,
        String state,
        String city,
        double temperature,
        String condition,
        int humidity,
        double windSpeed,
        int uvIndex,
        String sunrise,
        String sunset) {
}

package com.app.whether.weather;

public record WeatherDto(
        String country,
        String state,
        String city,
        double temp,
        double feelsLike,
        String desc,
        int humidity,
        double wind,
        String windDir,
        int uv,
        String sunrise,
        String sunset) {
}

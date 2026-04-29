package com.app.whether.weather;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class WeatherController {

    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }//constructor injection

    @GetMapping("/api/weather/current")
    public ResponseEntity<WeatherDto> getCurrentWeather(@RequestParam(value = "city", defaultValue = "Bellevue") String city){
        // Dummy data for demo v0.5
        WeatherDto mockWeather = new WeatherDto(
                "USA",
                "Washington",
                city,
                18.0,      // temp in °C
                17.5,      // feelsLike in °C
                "Partly Cloudy",
                45,        // humidity %
                12.5,      // wind km/h
                "NW",
                3,         // uv
                "06:15 AM",
                "20:10 PM"
        );

        return ResponseEntity.ok(mockWeather);
    }
}

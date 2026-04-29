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

        WeatherDto mockWeather = weatherService.getMockWeather(city);
        return ResponseEntity.ok(mockWeather);
    }
}

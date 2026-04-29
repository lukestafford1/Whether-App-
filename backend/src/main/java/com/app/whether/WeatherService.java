package com.app.whether;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    private WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }//constructor injection
}

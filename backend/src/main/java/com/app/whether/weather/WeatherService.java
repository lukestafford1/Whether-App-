package com.app.whether.weather;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
//    private WeatherRepository weatherRepository;

//    public WeatherService(WeatherRepository weatherRepository) {
//        this.weatherRepository = weatherRepository;
//    }//constructor injection

    public WeatherDto getMockWeather(String city) {
        return new WeatherDto("USA", "WA", city, 18.0, 17.5, "Sunny", 45, 12.0, "N", 3, "06:00", "20:00");
    }
}

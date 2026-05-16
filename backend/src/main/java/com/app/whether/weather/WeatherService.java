package com.app.whether.weather;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {
	public WeatherDto getMockWeather(String city) {
		// Added 1015 for pressure and 10 for visibility at the end
		return new WeatherDto("USA", "WA", city, 18.0, 17.5, "Sunny", 45, 12.0, "N", 3, "06:00", "20:00", 1015, 10);
	}
}
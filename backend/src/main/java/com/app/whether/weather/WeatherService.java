package com.app.whether.weather;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class WeatherService {

	private final RestClient restClient;
	private final ObjectMapper objectMapper;

	public WeatherService() {
		this.restClient = RestClient.create();
		this.objectMapper = new ObjectMapper();
	}

	@SuppressWarnings("unchecked")
	public WeatherDto getWeather(String location) {
		try {
			// STEP 1: URL Encode the location to handle spaces and commas safely
			String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
			String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + encodedLocation + "&count=1&format=json";

			String geoJson = restClient.get()
					.uri(geoUrl)
					.retrieve()
					.body(String.class);

			JsonNode geoRoot = objectMapper.readTree(geoJson);

			if (!geoRoot.has("results") || geoRoot.get("results").isEmpty()) {
				throw new RuntimeException("Location not found");
			}

			JsonNode locationData = geoRoot.get("results").get(0);
			double lat = locationData.get("latitude").asDouble();
			double lon = locationData.get("longitude").asDouble();
			String country = locationData.has("country") ? locationData.get("country").asText() : "";
			String state = locationData.has("admin1") ? locationData.get("admin1").asText() : "";
			String resolvedCity = locationData.get("name").asText();

			// STEP 2: Fetch weather
			String weatherUrl = String.format(
					"https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current=temperature_2m,apparent_temperature,relative_humidity_2m,wind_speed_10m,wind_direction_10m,surface_pressure,weather_code&daily=sunrise,sunset,uv_index_max&timezone=auto",
					lat, lon
			);

			String weatherJson = restClient.get()
					.uri(weatherUrl)
					.retrieve()
					.body(String.class);

			JsonNode weatherRoot = objectMapper.readTree(weatherJson);
			JsonNode current = weatherRoot.get("current");
			JsonNode daily = weatherRoot.get("daily");

			return new WeatherDto(
					country, state, resolvedCity,
					current.get("temperature_2m").asDouble(),
					current.get("apparent_temperature").asDouble(),
					parseWeatherCode(current.get("weather_code").asInt()),
					current.get("relative_humidity_2m").asInt(),
					current.get("wind_speed_10m").asDouble(),
					getWindDirection(current.get("wind_direction_10m").asInt()),
					(int) Math.round(daily.get("uv_index_max").get(0).asDouble()),
					daily.get("sunrise").get(0).asText().split("T")[1],
					daily.get("sunset").get(0).asText().split("T")[1],
					current.get("surface_pressure").asInt(), 10
			);

		} catch (HttpClientErrorException | HttpServerErrorException e) {
			return new WeatherDto("Unknown", "", location, 0.0, 0.0, "API error: " + e.getStatusCode(), 0, 0.0, "N", 0, "00:00", "00:00", 0, 0);
		} catch (Exception e) {
			return new WeatherDto("Unknown", "", location, 0.0, 0.0, "Error fetching/parsing data", 0, 0.0, "N", 0, "00:00", "00:00", 0, 0);
		}
	}

	private String parseWeatherCode(int code) {
		if (code == 0) return "Clear skies";
		if (code <= 3) return "Partly cloudy";
		if (code <= 49) return "Foggy";
		if (code <= 69) return "Rainy";
		if (code <= 79) return "Snowy";
		return "Stormy";
	}

	private String getWindDirection(int degrees) {
		String[] directions = {"North", "Northeast", "East", "Southeast", "South", "Southwest", "West", "Northwest"};
		return directions[(int) Math.round(((double) degrees % 360) / 45) % 8];
	}
}
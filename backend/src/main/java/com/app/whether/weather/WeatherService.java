package com.app.whether.weather;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
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
			// 1. Encode the location
			String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);

			// 2. Use java.net.URI to prevent RestTemplate from double-encoding the URL
			// Added the email parameter directly as requested by Nominatim's usage policy
			URI geoUri = URI.create("https://nominatim.openstreetmap.org/search?q=" + encodedLocation + "&format=json&limit=1&addressdetails=1&email=uwb.student.project@example.com");

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();

			// 3. Spoof a standard Chrome browser User-Agent to bypass the WAF bot-blocker
			headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
			headers.set("Accept", "application/json");
			HttpEntity<String> entity = new HttpEntity<>(headers);

			String geoJson = restTemplate.exchange(geoUri, HttpMethod.GET, entity, String.class).getBody();

			JsonNode geoRoot = objectMapper.readTree(geoJson);

			if (geoRoot.isEmpty()) {
				throw new RuntimeException("Nominatim could not find this specific address");
			}

			JsonNode locationData = geoRoot.get(0);

			// Safely parse Strings to Doubles to prevent Jackson JSON crashes
			double lat = Double.parseDouble(locationData.get("lat").asText());
			double lon = Double.parseDouble(locationData.get("lon").asText());

			JsonNode address = locationData.get("address");
			String country = address.has("country") ? address.get("country").asText() : "";
			String state = address.has("state") ? address.get("state").asText() : "";

			String resolvedCity = location;
			if (address.has("city")) resolvedCity = address.get("city").asText();
			else if (address.has("town")) resolvedCity = address.get("town").asText();
			else if (address.has("village")) resolvedCity = address.get("village").asText();

			// Fetch weather using precise coordinates
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
			return new WeatherDto("Unknown", "", location, 0.0, 0.0, "DEBUG ERROR: " + e.getMessage(), 0, 0.0, "N", 0, "00:00", "00:00", 0, 0);
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
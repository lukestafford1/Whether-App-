package com.app.whether.weather;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class WeatherController {

	private final WeatherService weatherService;
	private final SearchHistoryService searchHistoryService;

	public WeatherController(WeatherService weatherService, SearchHistoryService searchHistoryService) {
		this.weatherService = weatherService;
		this.searchHistoryService = searchHistoryService;
	}

	@GetMapping("/api/weather/current")
	public ResponseEntity<WeatherDto> getCurrentWeather(
			@RequestParam(value = "location", defaultValue = "Bellevue") String location,
			@AuthenticationPrincipal OAuth2User principal) {

		if (principal != null) {
			String email = principal.getAttribute("preferred_username");
			String name = principal.getAttribute("name");
			searchHistoryService.logUserSearch(email, name, location);
		}

		WeatherDto liveWeather = weatherService.getWeather(location);
		return ResponseEntity.ok(liveWeather);
	}

	@GetMapping("/api/weather/history")
	public ResponseEntity<List<String>> getSearchHistory(@AuthenticationPrincipal OAuth2User principal) {
		if (principal == null) {
			return ResponseEntity.ok(List.of());
		}

		String email = principal.getAttribute("preferred_username");
		return ResponseEntity.ok(searchHistoryService.getRecentSearches(email));
	}
}
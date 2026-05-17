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
	private final AppUserRepository userRepository;
	private final SearchHistoryRepository historyRepository;

	public WeatherController(WeatherService weatherService, AppUserRepository userRepository, SearchHistoryRepository historyRepository) {
		this.weatherService = weatherService;
		this.userRepository = userRepository;
		this.historyRepository = historyRepository;
	}

	@GetMapping("/api/weather/current")
	public ResponseEntity<WeatherDto> getCurrentWeather(
			@RequestParam(value = "city", defaultValue = "Bellevue") String city,
			@AuthenticationPrincipal OAuth2User principal) {

		// 1. Intercept user and save history IF they are logged in
		if (principal != null) {
			// Entra ID stores the user's email under "preferred_username" or "email"
			String email = principal.getAttribute("preferred_username");
			String name = principal.getAttribute("name");

			// Find them in the DB, or create a new row if it's their first time logging in
			AppUser user = userRepository.findByEmail(email)
					.orElseGet(() -> userRepository.save(new AppUser(email, name)));

			// Save the search
			historyRepository.save(new SearchHistory(user, city));
		}

		// 2. Fetch the live weather and return it to the frontend
		WeatherDto liveWeather = weatherService.getWeather(city);
		return ResponseEntity.ok(liveWeather);
	}

	@GetMapping("/api/weather/history")
	public ResponseEntity<List<String>> getSearchHistory(@AuthenticationPrincipal OAuth2User principal) {

		// 1. Grab the email from the Entra ID token
		String email = principal.getAttribute("preferred_username");

		// 2. Find the user in the DB. If they exist, get their history.
		return userRepository.findByEmail(email)
				.map(user -> {
					// Fetch history, extract just the city names, and put them into a List
					List<String> cities = historyRepository.findByUserOrderBySearchTimeDesc(user)
							.stream()
							.map(SearchHistory::getCitySearched)
							.toList();
					return ResponseEntity.ok(cities);
				})
				// 3. If the user isn't in the DB yet, just return an empty array []
				.orElse(ResponseEntity.ok(List.of()));
	}

}
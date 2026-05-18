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

		if (principal != null) {
			String email = principal.getAttribute("preferred_username");
			String name = principal.getAttribute("name");

			AppUser user = userRepository.findByEmail(email)
					.orElseGet(() -> userRepository.save(new AppUser(email, name)));

			SearchHistory existingSearch = historyRepository.findByUserAndCitySearchedIgnoreCase(user, city);

			if (existingSearch == null) {
				historyRepository.save(new SearchHistory(user, city));
			}
		}

		WeatherDto liveWeather = weatherService.getWeather(city);
		return ResponseEntity.ok(liveWeather);
	}

	@GetMapping("/api/weather/history")
	public ResponseEntity<List<String>> getSearchHistory(@AuthenticationPrincipal OAuth2User principal) {

		if (principal == null) {
			return ResponseEntity.ok(List.of());
		}

		String email = principal.getAttribute("preferred_username");

		return userRepository.findByEmail(email)
				.map(user -> {
					List<String> cities = historyRepository.findByUser(user)
							.stream()
							.map(SearchHistory::getCitySearched)
							.toList();
					return ResponseEntity.ok(cities);
				})
				.orElse(ResponseEntity.ok(List.of()));
	}
}
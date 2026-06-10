package com.app.whether.weather;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SearchHistoryService {

	private final AppUserRepository userRepository;
	private final SearchHistoryRepository historyRepository;

	public SearchHistoryService(AppUserRepository userRepository, SearchHistoryRepository historyRepository) {
		this.userRepository = userRepository;
		this.historyRepository = historyRepository;
	}

	@Transactional
	public void logUserSearch(String email, String name, String location) {
		AppUser user = userRepository.findByEmail(email)
				.orElseGet(() -> userRepository.save(new AppUser(email, name)));

		SearchHistory existingSearch = historyRepository.findByUserAndCitySearchedIgnoreCase(user, location);
		if (existingSearch == null) {
			historyRepository.save(new SearchHistory(user, location));
		}
	}

	public List<String> getRecentSearches(String email) {
		return userRepository.findByEmail(email)
				.map(user -> historyRepository.findTop10ByUserOrderByIdDesc(user)
						.stream()
						.map(SearchHistory::getCitySearched)
						.toList())
				.orElse(List.of());
	}
}
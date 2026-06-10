package com.app.whether.weather;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

	// Checks for duplicate cities when saving
	SearchHistory findByUserAndCitySearchedIgnoreCase(AppUser user, String citySearched);

	// Grabs the 10 most recent searches for the UI
	List<SearchHistory> findTop10ByUserOrderBySearchTimeDesc(AppUser user);
}
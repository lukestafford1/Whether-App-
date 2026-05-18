package com.app.whether.weather;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
	SearchHistory findByUserAndCitySearchedIgnoreCase(AppUser user, String citySearched);

	List<SearchHistory> findByUser(AppUser user);
}
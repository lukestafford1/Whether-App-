package com.app.whether.weather;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
	List<SearchHistory> findByUserOrderBySearchTimeDesc(AppUser user);
}
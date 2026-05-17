package com.app.whether.weather;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
public class SearchHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser user;

	private String citySearched;
	private LocalDateTime searchTime;

	public SearchHistory() {
	}

	public SearchHistory(AppUser user, String citySearched) {
		this.user = user;
		this.citySearched = citySearched;
		this.searchTime = LocalDateTime.now();
	}

	// Getters
	public Long getId() {
		return id;
	}

	public String getCitySearched() {
		return citySearched;
	}

	public LocalDateTime getSearchTime() {
		return searchTime;
	}
}
package com.app.whether.weather;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	private String name;

	public AppUser() {
	}

	public AppUser(String email, String name) {
		this.email = email;
		this.name = name;
	}

	// Getters
	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}
}
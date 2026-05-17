package com.app.whether.security;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final EntraIdRoleConverter entraIdRoleConverter;

	public SecurityConfig(EntraIdRoleConverter entraIdRoleConverter) { //constructor injection
		this.entraIdRoleConverter = entraIdRoleConverter;
	}

	//DEFINING THE SECURITY FILTER CHAIN:

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		try {
			return http
					.authorizeHttpRequests(authorize -> authorize
							.requestMatchers("/EntraIDLockTest.html").authenticated()
							.requestMatchers("/whether.html").authenticated()
							//Locking the actual backend API endpoint for security testing:
							.requestMatchers("/api/weather/**").authenticated()
							.anyRequest().permitAll()
					)
					.oauth2Login(oauth2 -> oauth2
							.userInfoEndpoint(userInfo -> userInfo
									.oidcUserService(this.entraIdRoleConverter)
							)
					)
					.logout(logout -> logout.logoutSuccessUrl("/"))
					.build();
		} catch (Exception e) {
			// Use the most specific Spring exception for bean failures
			throw new BeanCreationException("SecurityFilterChain", "Failed to build the security filter chain", e);
		}
	}

}

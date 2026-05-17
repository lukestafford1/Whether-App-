package com.app.whether.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WeatherControllerTest {

	private MockMvc mockMvc;
	private WeatherService weatherService;

	@BeforeEach
	void setup() {
		// 1. Mock the service AND the new repositories
		weatherService = Mockito.mock(WeatherService.class);
		AppUserRepository userRepository = Mockito.mock(AppUserRepository.class);
		SearchHistoryRepository historyRepository = Mockito.mock(SearchHistoryRepository.class);

		// 2. Inject all three into the controller
		WeatherController controller = new WeatherController(weatherService, userRepository, historyRepository);

		// 3. Build the test environment
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	void whenGetCurrentWeather_thenReturnFullFrontendPayload() throws Exception {
		String testCity = "London";


		WeatherDto mockResponse = new WeatherDto(
				"UK", "LND", testCity, 12.0, 10.0, "Cloudy", 78, 15.0, "NW", 3, "06:45", "19:30", 1015, 8
		);

		when(weatherService.getWeather(testCity)).thenReturn(mockResponse);


		mockMvc.perform(get("/api/weather/current")
						.param("city", testCity)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.city").value(testCity))
				.andExpect(jsonPath("$.temp").value(12.0))
				.andExpect(jsonPath("$.pressure").exists())
				.andExpect(jsonPath("$.visibility").exists());
	}
}
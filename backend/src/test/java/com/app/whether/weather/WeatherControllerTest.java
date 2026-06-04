package com.app.whether.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WeatherControllerTest {

	private MockMvc mockMvc;
	private WeatherService weatherService;
	private SearchHistoryService searchHistoryService;
	private WeatherController controller;

	@BeforeEach
	void setup() {
		weatherService = Mockito.mock(WeatherService.class);
		searchHistoryService = Mockito.mock(SearchHistoryService.class);

		controller = new WeatherController(weatherService, searchHistoryService);

		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
					@Override
					public boolean supportsParameter(MethodParameter parameter) {
						return parameter.getParameterType().isAssignableFrom(OAuth2User.class);
					}

					@Override
					public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mav, NativeWebRequest webReq, WebDataBinderFactory binder) {
						return null;
					}
				})
				.build();
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
				.andExpect(status().isOk());
	}


	@Test
	void whenCityIsEmpty_thenReturnFallbackResponse() {
		String emptyCity = "";
		WeatherDto fallbackResponse = new WeatherDto(
				"Unknown", "", emptyCity, 0.0, 0.0, "Error fetching/parsing data", 0, 0.0, "N", 0, "00:00", "00:00", 0, 0
		);

		// 1. Mock the service
		when(weatherService.getWeather(Mockito.any())).thenReturn(fallbackResponse);

		// 2. Call the controller directly like a standard Java method
		ResponseEntity<WeatherDto> response = controller.getCurrentWeather(emptyCity, null);

		// 3. Assert the exact Java object values
		assertNotNull(response.getBody());
		assertEquals("Unknown", response.getBody().country());
		assertEquals("Error fetching/parsing data", response.getBody().desc());
	}
}
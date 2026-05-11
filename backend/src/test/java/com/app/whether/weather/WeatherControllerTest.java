package com.app.whether.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultWeather() throws Exception {
        mockMvc.perform(get("/api/weather/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city", is("Bellevue")))
                .andExpect(jsonPath("$.temp", is(18.0)))
                .andExpect(jsonPath("$.country", is("USA")));
    }

    @Test
    public void shouldReturnRequestedCityWeather() throws Exception {
        mockMvc.perform(get("/api/weather/current?city=Seattle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city", is("Seattle")));
    }
}
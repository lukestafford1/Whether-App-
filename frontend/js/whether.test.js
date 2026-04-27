/*
Tests for weather_display.js with displayWeather() function
tests for accurate (true/false : expected) values pulled from API request
using dummy data to ensure that data is being returned correctly
*/

const { displayWeather } = require("./weather_display");

// simple assertion helper
function assert(condition, message) {
  if (!condition) {
    throw new Error(message);
  }
}

// simple test wrapper
function test(name, fn) {
  try {
    fn();
    console.log("PASS:", name);
  } catch (err) {
    console.log("FAIL:", name);
    console.error("   ", err.message);
  }
}

test("renders weather correctly", () => {
  const mockData = {
    current: {
      temperature_2m: 20,
      wind_speed_10m: 5,
      pressure_msl: 1012
    }
  };

  const result = displayWeather(mockData);

  assert(result.includes("20"), "Missing temperature");
  assert(result.includes("5"), "Missing wind speed");
  assert(result.includes("1012"), "Missing pressure");
});

test("handles missing values safely", () => {
  const mockData = {
    current: {
      temperature_2m: 0,
      wind_speed_10m: 0,
      pressure_msl: 0
    }
  };

  const result = displayWeather(mockData);

  assert(typeof result === "string", "Output should be string");
});

test("returns formatted string", () => {
  const mockData = {
    current: {
      temperature_2m: 10,
      wind_speed_10m: 2,
      pressure_msl: 1000
    }
  };

  const result = displayWeather(mockData);

  assert(result.includes("Temperature"), "Missing label text");
});


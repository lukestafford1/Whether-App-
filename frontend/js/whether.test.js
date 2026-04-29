/*
Tests for weather_displayfront.js with displayWeather() function
tests for accurate (true/false : expected) values pulled from API request
using dummy data to ensure that data is being returned correctly
*/

const { displayWeather } = require("./weather_display");

// simple DOM mock
global.document = {
  elements: {},

  getElementById(id) {
    if (!this.elements[id]) {
      this.elements[id] = { innerHTML: "" };
    }
    return this.elements[id];
  }
};

function assertTrue(condition, testName) {
  if (condition) {
    console.log("PASS:", testName);
  } else {
    console.log("FAIL:", testName);
  }
}

function runTests() {

  const mockWeather = {
    city: "Seattle",
    temp: 20,
    feelsLike: 19,
    desc: "Sunny",
    humidity: 40,
    wind: 5,
    windDir: "N",
    uv: 3,
    sunrise: "06:00",
    sunset: "20:00"
  };

  const result = displayWeather(mockWeather);

  assertTrue(result.includes("Seattle"), "City displayed");
  assertTrue(result.includes("20"), "Temperature displayed");
  assertTrue(result.includes("Sunny"), "Description displayed");
  assertTrue(result.includes("40"), "Humidity displayed");
  assertTrue(result.includes("5"), "Wind displayed");
  assertTrue(result.includes("06:00"), "Sunrise displayed");
  assertTrue(result.includes("20:00"), "Sunset displayed");

  try {
    displayWeather(null);
    assertTrue(false, "Invalid data should throw error");
  } catch (e) {
    assertTrue(true, "Invalid data throws error");
  }

}

runTests();
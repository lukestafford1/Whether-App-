/*
Function displayWeather() accepts 'data' variable with 'weather' object
returns weather->this to user for visualization and processing (if needed)

function displayWeather(data) {
  if (!data || typeof data !== "object") {
    throw new Error("Invalid weather data");
  }

  const resultHTML = `
    <h2>${data.city}</h2>
    <p>Temperature: ${data.temp} °C</p>
    <p>Feels Like: ${data.feelsLike} °C</p>
    <p>Description: ${data.desc}</p>
    <p>Humidity: ${data.humidity}%</p>
    <p>Wind: ${data.wind} km/h ${data.windDir}</p>
    <p>UV Index: ${data.uv}</p>
    <p>Sunrise: ${data.sunrise}</p>
    <p>Sunset: ${data.sunset}</p>
  `;

  const container = document.getElementById("weatherResult");
  if (container) {
    container.innerHTML = resultHTML;
  }

  return resultHTML;
}

if (typeof module !== "undefined") {
  module.exports = { displayWeather };
} */

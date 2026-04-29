/*
Function displayWeather() accepts 'data' variable with 'weather' object
returns weather->this to user for visualization and processing (if needed)
*/

function displayWeather(data) {
    if (!data || typeof data !== "object") {
    throw new Error("Invalid weather data");
    }

    const weather = data.current_weather;

    document.getElementById("weatherResult").innerHTML = `
        <h2>Current Weather</h2>
        <p>Temperature: ${weather.temperature} °C</p>
        <p>Wind Speed: ${weather.windspeed} km/h</p>
        <p>Wind Direction: ${weather.winddirection}°</p>
        <p>Humidity: ${weather.relative_humidity_2m} %</p>
        <p>Pressure: ${weather.pressure_msl} hPa</p>
    `;
}

// export function to test file
module.exports = { displayWeather };

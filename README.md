

**ClimatePulse** is an Android application providing weather forecasts for the user's current location or a specified city. The app fetches weather data using the OpenWeather API and displays it in a user-friendly interface.

## Features

- 🌍 Fetch current weather data based on the user's location.
- 🏙️ Fetch weather data for a specified city.
- 🌡️ Display temperature, weather conditions, real feel, and weather icon.
- 📅 Display forecasts for today.

## Prerequisites

- Android Studio
- Android SDK
- An API key from OpenWeather (sign up [here](https://home.openweathermap.org/users/sign_up)).

## Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/your-username/ClimatePulse.git
    ```

2. **Open the project in Android Studio.**

3. **Add your OpenWeather API key:**

    - Open `MainActivity.kt`.
    - Replace `"YOUR_API_KEY"` with your actual OpenWeather API key.

4. **Build and run the project** on an Android device or emulator.

## Usage

1. **Grant Location Permissions:**
    - When you first open the app, it will ask for location permissions. Grant the permissions to allow the app to fetch weather data based on your current location.

2. **View Current Weather Data:**
    - The app will display the current weather data, including temperature, weather conditions, real feel, and a weather icon.

3. **Search for Weather Data by City:**
    - Enter a city name in the text field and click the "Search" button to get weather data for that specific city.

## Code Structure

### MainActivity.kt

Handles fetching and displaying weather data:

- `getLastKnownLocation()`: Fetches the user's current location.
- `getCoordinatesFromLocationName(locationName: String)`: Converts a city name to its geographical coordinates.
- `fetchWeather(lat: Double, lon: Double)`: Fetches weather data based on geographical coordinates.
- `updateUI(weatherData: WeatherData)`: Updates the UI with fetched weather data.

### ApiClient.kt

Sets up the Retrofit client for making API calls to the OpenWeather API.

### WeatherService.kt

Defines the Retrofit service interface for fetching weather data.

### Models

Data classes representing the JSON response from the OpenWeather API:
- `WeatherData`
- `Main`
- `Weather`
- `Wind`
- `Sys`

### Layout

![Layout](https://github.com/user-attachments/assets/e202042f-3412-4937-a160-825a988cd5f2)

## Dependencies

- **Retrofit:** A type-safe HTTP client for Android and Java.
- **Gson:** A library for converting Java objects to and from JSON.
- **Glide:** An image loading and caching library for Android.
- **Lottie:** A library for animations.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgements

- [OpenWeather](https://openweathermap.org/) for providing the weather API.
- [Glide](https://github.com/bumptech/glide) for image loading and caching.
- [Retrofit](https://square.github.io/retrofit/) for the HTTP client.
- [Gson](https://github.com/google/gson) for JSON serialization/deserialization.

---

This version uses emojis to enhance readability, clear section headers for better navigation, and consistent formatting to improve the overall look and feel.

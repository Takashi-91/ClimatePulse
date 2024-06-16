package com.example.climatepulse

import WeatherService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val apiKey = "427288ae028f3aca2af2a779705331b4"
    private lateinit var weatherService: WeatherService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the UI elements
        val locationTextView: TextView = findViewById(R.id.location)
        val temperatureTextView: TextView = findViewById(R.id.temperature)
        val weatherIconImageView: ImageView = findViewById(R.id.imageViewWeatherIcon)
        val descriptionTextView: TextView = findViewById(R.id.description)
        val realFeelTextView: TextView = findViewById(R.id.real_feel)
        val tonightWeatherTextView: TextView = findViewById(R.id.tonight_weather)
        val tonightTempTextView: TextView = findViewById(R.id.tonight_temp)
        val tomorrowWeatherTextView: TextView = findViewById(R.id.tomorrow_weather)
        val currentTimeTextView: TextView = findViewById(R.id.current_time)

        // Set up logging
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        weatherService = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

        // Replace "CityName" with the desired city
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val weatherData = weatherService.getWeather("CityName", apiKey)
                withContext(Dispatchers.Main) {
                    updateUI(weatherData)
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "HTTP error: ${e.message()}", Toast.LENGTH_LONG).show()
                }
                Log.e("MainActivity", "HTTP error: ${e.message()}")
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
                Log.e("MainActivity", "Error: ${e.message}")
            }
        }
    }


    private fun updateUI(weatherData: WeatherData) {
        val locationTextView: TextView = findViewById(R.id.location)
        val temperatureTextView: TextView = findViewById(R.id.temperature)
        val weatherIconImageView: ImageView = findViewById(R.id.imageViewWeatherIcon)
        val descriptionTextView: TextView = findViewById(R.id.description)
        val realFeelTextView: TextView = findViewById(R.id.real_feel)
        val tonightWeatherTextView: TextView = findViewById(R.id.tonight_weather)
        val tonightTempTextView: TextView = findViewById(R.id.tonight_temp)
        val tomorrowWeatherTextView: TextView = findViewById(R.id.tomorrow_weather)
        val currentTimeTextView: TextView = findViewById(R.id.current_time)

        locationTextView.text = weatherData.name
        temperatureTextView.text = "${weatherData.main.temp.toInt()}°C"
        descriptionTextView.text = weatherData.weather[0].description
        realFeelTextView.text = "Real Feel: ${weatherData.main.feels_like.toInt()}°C"

        val iconUrl = "https://openweathermap.org/img/w/${weatherData.weather[0].icon}.png"
        Glide.with(this)
            .load(iconUrl)
            .into(weatherIconImageView)

        // Set other weather details if available
        // Note: Replace with actual data if available from the API response
        tonightWeatherTextView.text = "Tonight: Clear"
        tonightTempTextView.text = "10°C"
        tomorrowWeatherTextView.text = "Tomorrow: Sunny"
        currentTimeTextView.text = "Current Time: 12:00 PM"
    }
}

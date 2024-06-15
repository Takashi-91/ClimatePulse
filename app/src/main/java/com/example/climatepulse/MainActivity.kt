package com.example.climatepulse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val apiKey = "YOUR_OPENWEATHERMAP_API_KEY"
    private lateinit var weatherService: WeatherService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherService = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)

        // Replace "CityName" with the desired city
        GlobalScope.launch(Dispatchers.IO) {
            val weatherData = weatherService.getWeather("CityName", apiKey)
            withContext(Dispatchers.Main) {
                updateUI(weatherData)
            }
        }
    }

    private fun updateUI(weatherData: WeatherData) {
        findViewById<TextView>(R.id.textViewCity).text = weatherData.name
        findViewById<TextView>(R.id.textViewTemperature).text =
            "${weatherData.main.temp.toInt()}°C"
        val iconUrl = "https://openweathermap.org/img/w/${weatherData.weather[0].icon}.png"
             Glide.with(this)
            .load(iconUrl)
            .into(findViewById(R.id.imageViewWeatherIcon))
        }
    }


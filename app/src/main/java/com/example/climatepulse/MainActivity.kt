package com.example.climatepulse

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    private val apiKey = "f92512f698694c0838b111c64353a343"
    private lateinit var fusedLocationClient: FusedLocationProviderClient



    private lateinit var btnSearch: Button
    private lateinit var weatherIconImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lottieView1: LottieAnimationView = findViewById(R.id.lottieView1)
        lottieView1.setAnimation("animation2.json")
        lottieView1.repeatCount = LottieDrawable.INFINITE
        lottieView1.playAnimation()

        val lottieView2: LottieAnimationView = findViewById(R.id.lottieView2)
        lottieView2.setAnimation("animation1.json")
        lottieView2.repeatCount = LottieDrawable.INFINITE
        lottieView2.playAnimation()

        val lottieView3: LottieAnimationView = findViewById(R.id.lottieview3)
        lottieView3.setAnimation("animation4.json")
        lottieView3.repeatCount = LottieDrawable.INFINITE
        lottieView3.playAnimation()



        // Find the Spinner
        val etLocation : Spinner = findViewById(R.id.etLocation )

        val capitalCities=Locations.capitalCities
        // Create an ArrayAdapter using the list of capital cities and a default spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, capitalCities)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        etLocation .adapter = adapter
        btnSearch = findViewById(R.id.btnSearch)
        weatherIconImageView = findViewById(R.id.imageViewWeatherIcon)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnSearch.setOnClickListener {
            val locationName =
                etLocation.selectedItem.toString() // Assuming etLocation is a Spinner
            if (locationName.isNotEmpty()) {
                getCoordinatesFromLocationName(locationName)
            }

        }

            getLastKnownLocation()
        etLocation.setSelection(0)
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                fetchWeather(location.latitude, location.longitude)
            }
        }
    }

    private fun getCoordinatesFromLocationName(locationName: String) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocationName(locationName, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                fetchWeather(address.latitude, address.longitude)
            } else {
                Log.e("MainActivity", "No location found for $locationName")
            }
        }
    }


    private fun fetchWeather(lat: Double, lon: Double) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val weatherData = ApiClient.weatherService.getWeatherByCoordinates(lat, lon, apiKey)
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

    fun updateUI(weatherData: WeatherData) {
        val locationTextView: TextView = findViewById(R.id.location)
        val temperatureTextView: TextView = findViewById(R.id.temperature)
        val descriptionTextView: TextView = findViewById(R.id.description)
        val realFeelTextView: TextView = findViewById(R.id.real_feel)
        val humidtiyText: TextView = findViewById(R.id.humidtiyText)
        val windSpeed: TextView = findViewById(R.id.windSpeed)
        val windDrag: TextView = findViewById(R.id.windDrag)
        val minTemp: TextView = findViewById(R.id.minTemp)
        val maxTemp: TextView = findViewById(R.id.maxTemp)
        val sunSet:TextView=findViewById(R.id.sunSet)
        val sunRise:TextView=findViewById(R.id.sunRise)
        val country:TextView=findViewById(R.id.Country)
        val timeZone:TextView=findViewById(R.id.timeZone)
       val sunriseTime = convertUnixToTime(weatherData.sys.sunset)
       val sunsetTime = convertUnixToTime(weatherData.sys.sunrise)
     //  var timeZone=convertUnixToTimeZone(weatherData.main.timezone)

        locationTextView.text = weatherData.name
        temperatureTextView.text = "${weatherData.main.temp.toInt()}°C"
        descriptionTextView.text = weatherData.weather[0].description
        realFeelTextView.text = "Real Feel: ${weatherData.main.feels_like.toInt()}°C"
        humidtiyText.text="Humidtiy: ${weatherData.main.humidity}%"
        windSpeed.text="Speed: ${weatherData.wind.speed.toInt()}km/h"
        windDrag.text="Drag: ${weatherData.wind.deg}"
        minTemp.text="Min Temp ${weatherData.main.temp_min}°C"
        maxTemp.text="Max Temp: ${weatherData.main.temp_max}°C"
        country.text=weatherData.sys.country
        sunRise.text = "Sun Rise: $sunriseTime"
        sunSet.text = "Sun Set: $sunsetTime"
        timeZone.text="Time Zone${weatherData.main.timezone}"


        val iconUrl = "https://openweathermap.org/img/wn/${weatherData.weather[0].icon}.png"
        Glide.with(this)
            .load(iconUrl)
            .into(weatherIconImageView)
    }
    // Function to convert Unix timestamp to human-readable format
    fun convertUnixToTime(unixTime: Int): String {
        val date = Date(unixTime * 1000L) // Convert to milliseconds
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault()) // Desired format
        return sdf.format(date)

    }
   // fun convertUnixToTimeZone(unixTime: Int): String {

     //   val sd1 = SimpleDateFormat("HH:mm:ss", Locale.getDefault()) // Desired format
     //   sd1.timeZone = TimeZone.getDefault() // Set timezone to default or desired timezone
     //   return sd1.format(sd1)
    }




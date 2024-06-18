package com.example.climatepulse

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val apiKey = "f92512f698694c0838b111c64353a343"
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var etLocation: EditText
    private lateinit var btnSearch: Button
    private lateinit var weatherIconImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lottieView1: LottieAnimationView = findViewById(R.id.lottieView1)
        lottieView1.setAnimation("animation1.json")
        lottieView1.repeatCount = LottieDrawable.INFINITE
        lottieView1.playAnimation()

        val lottieView2: LottieAnimationView = findViewById(R.id.lottieView2)
        lottieView2.setAnimation("animation2.json")
        lottieView2.repeatCount = LottieDrawable.INFINITE
        lottieView2.playAnimation()

        val capitalCities = listOf(
            getString(R.string.select),
            "Afghanistan" to "Kabul",
            "Albania" to "Tirana",
            "Algeria" to "Algiers",
            "Andorra" to "Andorra la Vella",
            "Angola" to "Luanda",
            "Antigua and Barbuda" to "Saint John's",
            "Argentina" to "Buenos Aires",
            "Armenia" to "Yerevan",
            "Australia" to "Canberra",
            "Austria" to "Vienna",
            "Azerbaijan" to "Baku",
            "Bahamas" to "Nassau",
            "Bahrain" to "Manama",
            "Bangladesh" to "Dhaka",
            "Barbados" to "Bridgetown",
            "Belarus" to "Minsk",
            "Belgium" to "Brussels",
            "Belize" to "Belmopan",
            "Benin" to "Porto-Novo",
            "Bhutan" to "Thimphu",
            "Bolivia" to "Sucre",
            "Bosnia and Herzegovina" to "Sarajevo",
            "Botswana" to "Gaborone",
            "Brazil" to "Brasília",
            "Brunei" to "Bandar Seri Begawan",
            "Bulgaria" to "Sofia",
            "Burkina Faso" to "Ouagadougou",
            "Burundi" to "Gitega",
            "Cabo Verde" to "Praia",
            "Cambodia" to "Phnom Penh",
            "Cameroon" to "Yaoundé",
            "Canada" to "Ottawa",
            "Central African Republic" to "Bangui",
            "Chad" to "N'Djamena",
            "Chile" to "Santiago",
            "China" to "Beijing",
            "Colombia" to "Bogotá",
            "Comoros" to "Moroni",
            "Congo (Congo-Brazzaville)" to "Brazzaville",
            "Costa Rica" to "San José",
            "Croatia" to "Zagreb",
            "Cuba" to "Havana",
            "Cyprus" to "Nicosia",
            "Czechia (Czech Republic)" to "Prague",
            "Denmark" to "Copenhagen",
            "Djibouti" to "Djibouti",
            "Dominica" to "Roseau",
            "Dominican Republic" to "Santo Domingo",
            "Ecuador" to "Quito",
            "Egypt" to "Cairo",
            "El Salvador" to "San Salvador",
            "Equatorial Guinea" to "Malabo",
            "Eritrea" to "Asmara",
            "Estonia" to "Tallinn",
            "Eswatini" to "Mbabane",
            "Ethiopia" to "Addis Ababa",
            "Fiji" to "Suva",
            "Finland" to "Helsinki",
            "France" to "Paris",
            "Gabon" to "Libreville",
            "Gambia" to "Banjul",
            "Georgia" to "Tbilisi",
            "Germany" to "Berlin",
            "Ghana" to "Accra",
            "Greece" to "Athens",
            "Grenada" to "St. George's",
            "Guatemala" to "Guatemala City",
            "Guinea" to "Conakry",
            "Guinea-Bissau" to "Bissau",
            "Guyana" to "Georgetown",
            "Haiti" to "Port-au-Prince",
            "Honduras" to "Tegucigalpa",
            "Hungary" to "Budapest",
            "Iceland" to "Reykjavík",
            "India" to "New Delhi",
            "Indonesia" to "Jakarta",
            "Iran" to "Tehran",
            "Iraq" to "Baghdad",
            "Ireland" to "Dublin",
            "Israel" to "Jerusalem",
            "Italy" to "Rome",
            "Jamaica" to "Kingston",
            "Japan" to "Tokyo",
            "Jordan" to "Amman",
            "Kazakhstan" to "Nur-Sultan",
            "Kenya" to "Nairobi",
            "Kiribati" to "South Tarawa",
            "Korea, North" to "Pyongyang",
            "Korea, South" to "Seoul",
            "Kosovo" to "Pristina",
            "Kuwait" to "Kuwait City",
            "Kyrgyzstan" to "Bishkek",
            "Laos" to "Vientiane",
            "Latvia" to "Riga",
            "Lebanon" to "Beirut",
            "Lesotho" to "Maseru",
            "Liberia" to "Monrovia",
            "Libya" to "Tripoli",
            "Liechtenstein" to "Vaduz",
            "Lithuania" to "Vilnius",
            "Luxembourg" to "Luxembourg",
            "Madagascar" to "Antananarivo",
            "Malawi" to "Lilongwe",
            "Malaysia" to "Kuala Lumpur",
            "Maldives" to "Malé",
            "Mali" to "Bamako",
            "Malta" to "Valletta",
            "Marshall Islands" to "Majuro",
            "Mauritania" to "Nouakchott",
            "Mauritius" to "Port Louis",
            "Mexico" to "Mexico City",
            "Micronesia" to "Palikir",
            "Moldova" to "Chisinau",
            "Monaco" to "Monaco",
            "Mongolia" to "Ulaanbaatar",
            "Montenegro" to "Podgorica",
            "Morocco" to "Rabat",
            "Mozambique" to "Maputo",
            "Myanmar (formerly Burma)" to "Naypyidaw",
            "Namibia" to "Windhoek",
            "Nauru" to "Yaren",
            "Nepal" to "Kathmandu",
            "Netherlands" to "Amsterdam",
            "New Zealand" to "Wellington",
            "Nicaragua" to "Managua",
            "Niger" to "Niamey",
            "Nigeria" to "Abuja",
            "North Macedonia" to "Skopje",
            "Norway" to "Oslo",
            "Oman" to "Muscat",
            "Pakistan" to "Islamabad",
            "Palau" to "Ngerulmud",
            "Palestine State" to "Jerusalem",
            "Panama" to "Panama City",
            "Papua New Guinea" to "Port Moresby",
            "Paraguay" to "Asuncion",
            "Peru" to "Lima",
            "Philippines" to "Manila",
            "Poland" to "Warsaw",
            "Portugal" to "Lisbon",
            "Qatar" to "Doha",
            "Romania" to "Bucharest",
            "Russia" to "Moscow",
            "Rwanda" to "Kigali",
            "Saint Kitts and Nevis" to "Basseterre",
            "Saint Lucia" to "Castries",
            "Saint Vincent and the Grenadines" to "Kingstown",
            "Samoa" to "Apia",
            "San Marino" to "San Marino",
            "Sao Tome and Principe" to "Sao Tome",
            "Saudi Arabia" to "Riyadh",
            "Senegal" to "Dakar",
            "Serbia" to "Belgrade",
            "Seychelles" to "Victoria",
            "Sierra Leone" to "Freetown",
            "Singapore" to "Singapore",
            "Slovakia" to "Bratislava",
            "Slovenia" to "Ljubljana",
            "Solomon Islands" to "Honiara",
            "Somalia" to "Mogadishu",
            "South Africa" to "Pretoria",
            "South Sudan" to "Juba",
            "Spain" to "Madrid",
            "Sri Lanka" to "Colombo",
            "Sudan" to "Khartoum",
            "Suriname" to "Paramaribo",
            "Sweden" to "Stockholm",
            "Switzerland" to "Bern",
            "Syria" to "Damascus",
            "Taiwan" to "Taipei",
            "Tajikistan" to "Dushanbe",
            "Tanzania" to "Dodoma",
            "Thailand" to "Bangkok",
            "Timor-Leste" to "Dili",
            "Togo" to "Lome",
            "Tonga" to "Nuku'alofa",
            "Trinidad and Tobago",
            // Add more capital cities here
        )
        // Find the Spinner
        val etLocation : Spinner = findViewById(R.id.etLocation )

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

    private fun updateUI(weatherData: WeatherData) {
        val locationTextView: TextView = findViewById(R.id.location)
        val temperatureTextView: TextView = findViewById(R.id.temperature)
        val descriptionTextView: TextView = findViewById(R.id.description)
        val realFeelTextView: TextView = findViewById(R.id.real_feel)
        val humidtiyText: TextView = findViewById(R.id.humidtiyText)
        val windText: TextView = findViewById(R.id.windText)
        val windSpeed: TextView = findViewById(R.id.windSpeed)
        val windDrag: TextView = findViewById(R.id.windDrag)
        val minTemp: TextView = findViewById(R.id.minTemp)
        val maxTemp: TextView = findViewById(R.id.maxTemp)

        locationTextView.text = weatherData.name
        temperatureTextView.text = "${weatherData.main.temp.toInt()}°C"
        descriptionTextView.text = weatherData.weather[0].description
        realFeelTextView.text = "Real Feel: ${weatherData.main.feels_like.toInt()}°C"
        windText.text="Wind: ${weatherData.main.wind.toInt()}%"
        humidtiyText.text="Humidtiy: ${weatherData.main.humidity}%"
        windSpeed.text="Speed: ${weatherData.wind.speed.toInt()}km/h"
        windDrag.text="Drag: ${weatherData.wind.deg}"
        minTemp.text="Min Temp ${weatherData.main.temp_min}°C"
        maxTemp.text="Max Temp: ${weatherData.main.temp_max}°C"


        val iconUrl = "https://openweathermap.org/img/wn/${weatherData.weather[0].icon}.png"
        Glide.with(this)
            .load(iconUrl)
            .into(weatherIconImageView)
    }
}

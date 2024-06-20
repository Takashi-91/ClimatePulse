package com.example.climatepulse

import java.util.TimeZone

data class WeatherData(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val sys: Sys,
    val visibility: Int,
    val dt: Long

) {


}

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val wind:Double,
    val timezone:Int
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Float,
    val deg: Int,
    val gust:Float
)

data class Sys(
    val country: String,
    val sunrise: Int,
    val sunset: Int
) {

}

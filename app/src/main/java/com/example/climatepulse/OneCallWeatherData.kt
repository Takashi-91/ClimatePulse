import com.example.climatepulse.Weather

data class OneCallWeatherData(
    val daily: List<DailyWeather>
)

data class DailyWeather(
    val dt: Long,
    val temp: Temp,
    val weather: List<Weather>
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double
)

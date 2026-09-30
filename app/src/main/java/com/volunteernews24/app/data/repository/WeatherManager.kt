package com.volunteernews24.app.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class WeatherInfo(
    val city: String,
    val temperatureCelsius: Double,
    val iconUrl: String,
    val error: String? = null
)

object WeatherManager {
    @SuppressLint("MissingPermission")
    suspend fun fetchCurrentWeather(context: Context): WeatherInfo? = withContext(Dispatchers.IO) {
        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            
            // Fetch current location with high accuracy
            val location = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
            
            if (location == null) {
                return@withContext WeatherInfo("", 0.0, "", error = "Failed to get location. Please ensure GPS is enabled.")
            }

            val lat = location.latitude
            val lon = location.longitude

            // Get Weather (OpenWeatherMap)
            val apiKey = "5a4ea810c2f0e628119f7e37d3b4541e"
            val weatherUrl = URL("https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric")
            val weatherConn = weatherUrl.openConnection() as HttpURLConnection
            weatherConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android) VolunteerNews24App")
            weatherConn.connectTimeout = 3000
            weatherConn.readTimeout = 3000
            
            val weatherResponse = weatherConn.inputStream.bufferedReader().readText()
            val weatherJson = JSONObject(weatherResponse)
            val main = weatherJson.optJSONObject("main")
            val temperature = main?.optDouble("temp", Double.NaN)
            val city = weatherJson.optString("name", "Unknown")
            
            val weatherArray = weatherJson.optJSONArray("weather")
            val iconCode = if (weatherArray != null && weatherArray.length() > 0) {
                weatherArray.getJSONObject(0).optString("icon", "")
            } else {
                ""
            }
            
            val iconUrl = if (iconCode.isNotEmpty()) "https://openweathermap.org/img/wn/$iconCode@2x.png" else ""

            if (temperature == null || temperature.isNaN()) return@withContext null
            val roundedTemp = Math.round(temperature * 10.0) / 10.0

            WeatherInfo(city, roundedTemp, iconUrl)
        } catch (e: SecurityException) {
            WeatherInfo("", 0.0, "", error = "Location permission denied.")
        } catch (e: Exception) {
            e.printStackTrace()
            WeatherInfo("", 0.0, "", error = "Failed to fetch weather data. Check connection.")
        }
    }
}

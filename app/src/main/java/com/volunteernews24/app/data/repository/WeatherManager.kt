package com.volunteernews24.app.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class WeatherInfo(
    val city: String,
    val temperatureCelsius: Double
)

object WeatherManager {
    suspend fun fetchCurrentWeather(): WeatherInfo? = withContext(Dispatchers.IO) {
        try {
            // 1. Get Location (IP based)
            val ipUrl = URL("https://ipapi.co/json/")
            val ipConn = ipUrl.openConnection() as HttpURLConnection
            ipConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android) VolunteerNews24App")
            val ipApiResponse = ipConn.inputStream.bufferedReader().readText()
            
            val ipJson = JSONObject(ipApiResponse)
            val city = ipJson.optString("city", "Unknown")
            
            // ipapi returns string or double for coordinates sometimes, handle both safely
            val lat = ipJson.optDouble("latitude", Double.NaN)
            val lon = ipJson.optDouble("longitude", Double.NaN)
            
            if (lat.isNaN() || lon.isNaN()) return@withContext null

            // 2. Get Weather (Open-Meteo)
            val weatherUrl = URL("https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current_weather=true")
            val weatherConn = weatherUrl.openConnection() as HttpURLConnection
            weatherConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android) VolunteerNews24App")
            val weatherResponse = weatherConn.inputStream.bufferedReader().readText()
            
            val weatherJson = JSONObject(weatherResponse)
            val current = weatherJson.optJSONObject("current_weather")
            val temperature = current?.optDouble("temperature", Double.NaN)
            
            if (temperature == null || temperature.isNaN()) return@withContext null

            WeatherInfo(city, temperature)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

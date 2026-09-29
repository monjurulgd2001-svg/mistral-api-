package com.volunteernews24.app.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class WeatherInfo(
    val city: String,
)

object WeatherManager {
    suspend fun fetchCurrentWeather(): WeatherInfo? = withContext(Dispatchers.IO) {
        try {
            var lat = 23.8103 // Default to Dhaka
            var lon = 90.4125
            var city = "Dhaka"

            try {
                // 1. Get Location (IP based)
                val ipUrl = URL("https://ipapi.co/json/")
                val ipConn = ipUrl.openConnection() as HttpURLConnection
                ipConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android) VolunteerNews24App")
                ipConn.connectTimeout = 3000
                ipConn.readTimeout = 3000
                
                if (ipConn.responseCode == 200) {
                    val ipApiResponse = ipConn.inputStream.bufferedReader().readText()
                    val ipJson = JSONObject(ipApiResponse)
                    
                    val parsedLat = ipJson.optDouble("latitude", Double.NaN)
                    val parsedLon = ipJson.optDouble("longitude", Double.NaN)
                    
                    if (!parsedLat.isNaN() && !parsedLon.isNaN()) {
                        lat = parsedLat
                        lon = parsedLon
                        city = ipJson.optString("city", "Dhaka")
                    }
                }
            } catch (e: Exception) {
                // Fallback to Dhaka on failure (e.g. rate limit, network issue)
                e.printStackTrace()
            }

            // 2. Get Weather (Open-Meteo)
            val weatherUrl = URL("https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current_weather=true")
            val weatherConn = weatherUrl.openConnection() as HttpURLConnection
            weatherConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android) VolunteerNews24App")
            weatherConn.connectTimeout = 3000
            weatherConn.readTimeout = 3000
            
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

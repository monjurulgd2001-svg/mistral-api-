package com.volunteernews24.app.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class WeatherInfo(
    val city: String,
    val temperatureCelsius: Double,
    val iconUrl: String
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

            // 2. Get Weather (OpenWeatherMap)
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
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

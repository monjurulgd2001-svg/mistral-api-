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

            var finalLocationName = city
            try {
                var foundLocation = false
                try {
                    val geocoder = android.location.Geocoder(context, java.util.Locale("bn", "BD"))
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(lat, lon, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val subLocality = address.subLocality ?: ""
                        val locality = address.locality ?: ""
                        val subAdmin = address.subAdminArea ?: ""
                        
                        val unionName = subLocality
                        val districtName = if (subAdmin.isNotEmpty()) subAdmin else locality
                        
                        if (unionName.isNotEmpty() && districtName.isNotEmpty()) {
                            finalLocationName = "$unionName, $districtName"
                            foundLocation = true
                        } else if (unionName.isNotEmpty()) {
                            finalLocationName = unionName
                            foundLocation = true
                        } else if (districtName.isNotEmpty()) {
                            finalLocationName = districtName
                            foundLocation = true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (!foundLocation) {
                    val mapsApiKey = com.volunteernews24.app.BuildConfig.MAPS_API_KEY
                    if (mapsApiKey.isNotEmpty()) {
                        val geoUrl = URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=$lat,$lon&key=$mapsApiKey")
                        val geoConn = geoUrl.openConnection() as HttpURLConnection
                        geoConn.connectTimeout = 3000
                        geoConn.readTimeout = 3000
                        val geoResponse = geoConn.inputStream.bufferedReader().readText()
                        val geoJson = JSONObject(geoResponse)
                        
                        if (geoJson.optString("status") == "REQUEST_DENIED") {
                            android.util.Log.e("WeatherManager", "Geocoding API Key Denied: ${geoJson.optString("error_message")}")
                        }
                        
                        var unionName = ""
                        var districtName = ""
                        
                        val results = geoJson.optJSONArray("results")
                        if (results != null && results.length() > 0) {
                            val addressComponents = results.getJSONObject(0).optJSONArray("address_components")
                            if (addressComponents != null) {
                                for (i in 0 until addressComponents.length()) {
                                    val component = addressComponents.getJSONObject(i)
                                    val types = component.optJSONArray("types")
                                    val longName = component.optString("long_name")
                                    if (types != null) {
                                        var isSublocality = false
                                        var isDistrict = false
                                        for (j in 0 until types.length()) {
                                            val type = types.optString(j)
                                            if (type == "sublocality_level_1" || type == "administrative_area_level_4" || type == "administrative_area_level_3" || type.equals("union", ignoreCase = true) || longName.contains("Union", ignoreCase = true) || longName.contains("ইউনিয়ন")) {
                                                isSublocality = true
                                            }
                                            if (type == "administrative_area_level_2" || type == "locality") {
                                                isDistrict = true
                                            }
                                        }
                                        if (isSublocality && unionName.isEmpty()) unionName = longName
                                        if (isDistrict && districtName.isEmpty()) districtName = longName
                                    }
                                }
                            }
                        }
                        
                        if (unionName.isNotEmpty() && districtName.isNotEmpty()) {
                            finalLocationName = "$unionName, $districtName"
                        } else if (unionName.isNotEmpty()) {
                            finalLocationName = unionName
                        } else if (districtName.isNotEmpty()) {
                            finalLocationName = districtName
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            WeatherInfo(finalLocationName, roundedTemp, iconUrl)
        } catch (e: SecurityException) {
            WeatherInfo("", 0.0, "", error = "Location permission denied.")
        } catch (e: Exception) {
            e.printStackTrace()
            WeatherInfo("", 0.0, "", error = "Failed to fetch weather data. Check connection.")
        }
    }
}

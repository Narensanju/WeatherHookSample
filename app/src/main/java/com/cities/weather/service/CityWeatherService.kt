package com.cities.weather.service

import android.location.Location
import com.cities.weather.model.weatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface CityWeatherService {

    @GET("data/2.5/weather?")
    fun getweatherDetails(@Query("lat") lat: String?, @Query("lon") lon: String?, @Query("APPID") app_id: String?): Call<weatherData.WeatherData?>?
}

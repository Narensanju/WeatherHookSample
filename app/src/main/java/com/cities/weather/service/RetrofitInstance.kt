package com.cities.weather.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
        private var retrofit: Retrofit? = null

        private const val BASE_URL = "http://api.openweathermap.org/"
    val service: CityWeatherService
            get() {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
                return retrofit!!.create(CityWeatherService::class.java)
            }
}
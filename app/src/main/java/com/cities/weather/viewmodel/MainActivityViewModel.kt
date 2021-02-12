package com.cities.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cities.weather.model.BookmarkedCity
import com.cities.weather.model.CityRepository
import com.cities.weather.model.weatherData

class MainActivityViewModel(application: Application) :
        AndroidViewModel(application) {

    fun getWeatherDetails(latitude: Double, longitute: Double) {
        cityRepository.getWeatherDetails(latitude, longitute)
    }

    fun deleteSelectedCity(city: BookmarkedCity) {
        cityRepository.getMutableLiveDataAfterCityDeletion(city)
    }

    fun getCityByName(searchedCity: String) {
        cityRepository.getSearchedCity(searchedCity)
    }

    val getBookmarkedAfterDeletion: LiveData<List<BookmarkedCity>>
        get() = cityRepository.getMutableLiveDataAfterDeletion()

    private val cityRepository: CityRepository

    val allBookmarkedCity: LiveData<List<BookmarkedCity>>
        get() = cityRepository.getBookmarkedCities()

    val getWeatherLiveData: LiveData<weatherData.WeatherData?>?
        get() = cityRepository.getWeatherLiveData()

    val getSearchedCityDetails: MutableLiveData<List<BookmarkedCity>>
        get() = cityRepository.getCitySearchedFor()

    init {
        cityRepository = CityRepository(application)
        allBookmarkedCity.value
        getWeatherLiveData?.value
    }
}
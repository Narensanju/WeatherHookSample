package com.cities.weather.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cities.weather.R
import com.cities.weather.service.CityWeatherService
import com.cities.weather.service.RetrofitInstance
import com.cities.weather.utils.Utils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityRepository(var application: Application) {

    private val getBookmarkedCities: MutableLiveData<List<BookmarkedCity>> = MutableLiveData()
    private val mutableweatherData: MutableLiveData<weatherData.WeatherData> = MutableLiveData()
    private val mutableLiveDataafterDeletion: MutableLiveData<List<BookmarkedCity>> = MutableLiveData()
    private var mutableweatherDataSearch:MutableLiveData<List<BookmarkedCity>>  = MutableLiveData()


    fun getBookmarkedCities(): MutableLiveData<List<BookmarkedCity>> {
        getBookmarkedCities.value = Utils.getCity(application)
        return getBookmarkedCities
    }


    fun getWeatherDetails(lat: Double, lon: Double) {
        val cityWeatherService: CityWeatherService = RetrofitInstance.service
        val call: Call<weatherData.WeatherData?>? = cityWeatherService.getweatherDetails(
                lat.toString(),
                lon.toString(),
                application.applicationContext.getString(R.string.weatherapi)
        )
        call!!.enqueue(object : Callback<weatherData.WeatherData?> {
            override fun onResponse(
                    call: Call<weatherData.WeatherData?>?,
                    response: Response<weatherData.WeatherData?>
            ) {
                if (response.code() == 200) {
                    var weatherResponse = response.body();
                    Log.e("weather", Gson().toJson(weatherResponse))
                    mutableweatherData.value = weatherResponse
                }
            }

            override fun onFailure(call: Call<weatherData.WeatherData?>, t: Throwable) {
                mutableweatherData.value = null
            }
        })
    }


    fun getWeatherLiveData(): LiveData<weatherData.WeatherData?>? {
        return mutableweatherData
    }

    fun getMutableLiveDataAfterCityDeletion(city: BookmarkedCity): LiveData<List<BookmarkedCity>> {
        var list = Utils.getCity(application)
        list!!.removeAll {
            it.cityName.equals(city.cityName)
        }
        mutableLiveDataafterDeletion.value = list
        Utils.saveCity(application, list)
        var listAfterDeletion = Utils.getCity(application)

        mutableLiveDataafterDeletion.value=listAfterDeletion
        return mutableLiveDataafterDeletion
    }

    fun getMutableLiveDataAfterDeletion(): LiveData<List<BookmarkedCity>> {
        return mutableLiveDataafterDeletion
    }


    fun getSearchedCity(searchedCity: String): MutableLiveData<List<BookmarkedCity>> {
       var citiesBoomarked: List<BookmarkedCity>? = getBookmarkedCities.value
        var citiesMatched:MutableList<BookmarkedCity> = ArrayList<BookmarkedCity>()

        for(city in citiesBoomarked!!.indices)
        {
            if(citiesBoomarked[city].cityName.toLowerCase().contains(searchedCity))
            {
                citiesMatched.add(citiesBoomarked[city])
            }
        }
        mutableweatherDataSearch.value=citiesMatched
        return mutableweatherDataSearch
    }

    fun getCitySearchedFor(): MutableLiveData<List<BookmarkedCity>> {
        return mutableweatherDataSearch

    }

}
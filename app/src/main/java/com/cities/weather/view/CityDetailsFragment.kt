package com.cities.weather.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cities.weather.MainActivity
import com.cities.weather.R
import com.cities.weather.model.BookmarkedCity
import com.cities.weather.model.weatherData.WeatherData
import com.cities.weather.viewmodel.MainActivityViewModel
import java.util.*

class CityDetailsFragment(context: Context, var city: BookmarkedCity) : Fragment() {

    private var mainActivityViewModel: MainActivityViewModel? = null
    private var weatherDetail: WeatherData? = null
    lateinit var rootView: View;


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //inflating
        rootView = inflater.inflate(R.layout.weather_details, container, false)
        (activity as MainActivity).supportActionBar?.hide()
        (activity as MainActivity).disablefabButton()
        mainActivityViewModel = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        mainActivityViewModel!!.getWeatherDetails(city.latitude, city.longitute)
        mainActivityViewModel!!.getWeatherLiveData!!.observe(viewLifecycleOwner, Observer { ci ->
            Log.e("cl", ci!!.clouds.toString())
            weatherDetail = ci
            setData()
        }
        )
        return rootView
    }

    fun setData() {
        rootView.findViewById<TextView>(R.id.txt_cityname).text = "TODAY," + Calendar.getInstance().get(Calendar.DATE) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR)
        rootView.findViewById<TextView>(R.id.txt_day).text = city.cityName
        rootView.findViewById<TextView>(R.id.txt_weather).text =
                weatherDetail!!.weather[0].description
        rootView.findViewById<TextView>(R.id.txt_temperature).text = "" + Math.round((weatherDetail!!.main.temp - 273.15) * 100.0) / 100.0 + "\u2103"
        rootView.findViewById<TextView>(R.id.txt_humidity).text = "" + weatherDetail!!.main.humidity + "%"
        rootView.findViewById<TextView>(R.id.txt_wind).text = "" + weatherDetail!!.wind.speed + " m/s" + " with wind direction " + weatherDetail!!.wind.deg + " degress"
        rootView.findViewById<TextView>(R.id.txt_clouds).text = "" + weatherDetail!!.clouds.all + "%"
    }


}





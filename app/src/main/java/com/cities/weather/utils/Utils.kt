package com.cities.weather.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.cities.weather.constants.AppConstants
import com.cities.weather.model.BookmarkedCity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Utils {


    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getSharedPreference(context: Context?): SharedPreferences {
            return context!!.getSharedPreferences("data", Context.MODE_MULTI_PROCESS)
        }

    fun saveCity(mapsActivity: Application, cityName: ArrayList<BookmarkedCity>) {
        val gson = Gson()
        val cityListString = gson.toJson(cityName)
        val mPrefs = getSharedPreference(mapsActivity)
        mPrefs.edit().putString(AppConstants.city, cityListString).commit()
    }

    fun getCity(context:Context): java.util.ArrayList<BookmarkedCity>?
    {
        val mPrefs = getSharedPreference(context)
        val cityListString = mPrefs.getString(AppConstants.city, "oopsDintWork")
        return if (cityListString.equals("oopsDintWork", ignoreCase = true)) {
            null
        } else {
            val type = object : TypeToken<java.util.ArrayList<BookmarkedCity?>?>() {}.type
            Gson().fromJson<java.util.ArrayList<BookmarkedCity>>(cityListString, type)
        }
    }



}
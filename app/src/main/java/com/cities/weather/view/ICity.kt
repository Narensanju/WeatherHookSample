package com.cities.weather.view

import com.cities.weather.model.BookmarkedCity

interface ICity {

    fun onCityClick(city:BookmarkedCity,viewOrAdd:Int)// 1 for view and 2 for delete
}
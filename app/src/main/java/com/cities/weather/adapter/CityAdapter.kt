package com.cities.weather.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cities.weather.R
import com.cities.weather.constants.AppConstants
import com.cities.weather.model.BookmarkedCity
import com.cities.weather.view.ICity
import kotlin.collections.ArrayList

class CityAdapter(
        private val context: Context,
        private val cityArrayList: ArrayList<BookmarkedCity?>?,
        val iCity: ICity
) :
    RecyclerView.Adapter<CityAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_list_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {
        holder.movieTitle.setText(cityArrayList?.get(position)!!.cityName)

    }

    override fun getItemCount(): Int {
        return cityArrayList!!.size
    }

    inner class MovieViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var movieTitle: TextView
        var deleteCity:ImageView
        init {
            movieTitle = itemView.findViewById<View>(R.id.tvTitle) as TextView
            deleteCity=itemView.findViewById<View>(R.id.img_delete) as ImageView

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selctedCity = cityArrayList?.get(position)
                    if (selctedCity != null) {
                        iCity.onCityClick(selctedCity,AppConstants.View)
                    }

                }
            }
            deleteCity.setOnClickListener{
                val selctedCity = cityArrayList?.get(position)
                if (selctedCity != null) {
                    iCity.onCityClick(selctedCity,AppConstants.DELETE)
                }
            }
        }
    }

}

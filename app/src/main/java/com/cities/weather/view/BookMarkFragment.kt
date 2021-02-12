package com.cities.weather.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cities.weather.MainActivity
import com.cities.weather.R
import com.cities.weather.adapter.CityAdapter
import com.cities.weather.constants.AppConstants
import com.cities.weather.model.BookmarkedCity
import com.cities.weather.viewmodel.MainActivityViewModel

class BookMarkFragment() : Fragment() {

    private var cities: ArrayList<BookmarkedCity?>? = null
    private var weatherDetails = null
    private var recyclerView: RecyclerView? = null
    private var txtNoData: TextView? = null
    private var movieAdapter: CityAdapter? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var mainActivityViewModel: MainActivityViewModel? = null
    lateinit var rootView: View;
    private var iCity: ICity? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        iCity = context as ICity
    }

    override fun onDetach() {
        iCity = null
        super.onDetach()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_bookmark, container, false)
        (activity as MainActivity).supportActionBar?.title = AppConstants.TITLE_SAVED_CITIES
        mainActivityViewModel = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_layout)
        (rootView.findViewById<View>(R.id.edt_searchby) as EditText).addTextChangedListener(object :
                TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 0) {
                    getBookmarkedCities()
                } else if (s!!.length > 0 && s!!.length < 3) {
                    mainActivityViewModel!!.getCityByName(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { getBookmarkedCities() })
        updateIfDeltion()
        mainActivityViewModel!!.getSearchedCityDetails
                .observe(viewLifecycleOwner,
                        Observer<List<BookmarkedCity?>?> { moviesFromLiveData ->
                            cities = moviesFromLiveData as ArrayList<BookmarkedCity?>?
                            showOnRecyclerView()
                        })
        return rootView;
    }

    override fun onResume() {
        super.onResume()
        getBookmarkedCities()

    }

    private fun updateIfDeltion() {
        mainActivityViewModel!!.getBookmarkedAfterDeletion
                .observe(viewLifecycleOwner,
                        Observer<List<BookmarkedCity?>?> { moviesFromLiveData ->
                            cities = moviesFromLiveData as ArrayList<BookmarkedCity?>?
                            showOnRecyclerView()
                        })
    }

    private fun getBookmarkedCities() {
        //if no data found  display empty
        mainActivityViewModel!!.allBookmarkedCity
                .observe(viewLifecycleOwner,
                        Observer<List<BookmarkedCity?>?> { moviesFromLiveData ->
                            cities = moviesFromLiveData as ArrayList<BookmarkedCity?>?
                            showOnRecyclerView()
                        })
    }


    private fun showOnRecyclerView() {
        recyclerView = rootView.findViewById<View>(R.id.rvbookmarkedcities) as RecyclerView
        txtNoData = rootView.findViewById<View>(R.id.tvNoData) as TextView
        if (cities != null) {
            if (cities!!.size > 0) {
                recyclerView!!.visibility = View.VISIBLE
                movieAdapter = iCity?.let { CityAdapter(requireContext(), cities, it) }
                recyclerView!!.setAdapter(movieAdapter)
                movieAdapter!!.notifyDataSetChanged()
            } else {
                txtNoData!!.visibility = View.VISIBLE
                recyclerView!!.visibility = View.GONE
            }
        } else {
            txtNoData!!.visibility = View.VISIBLE
            recyclerView!!.visibility = View.GONE
        }
        swipeRefreshLayout!!.isRefreshing = false
        if (this.resources
                        .configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        ) {
            recyclerView!!.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            recyclerView!!.layoutManager = GridLayoutManager(requireContext(), 4)
        }
        recyclerView!!.itemAnimator = DefaultItemAnimator()
    }

    fun deleteSelectedCity(city: BookmarkedCity) {
        mainActivityViewModel!!.deleteSelectedCity(city)

    }


}
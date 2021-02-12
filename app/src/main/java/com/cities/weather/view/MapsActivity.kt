package com.cities.weather.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cities.weather.R
import com.cities.weather.constants.AppConstants
import com.cities.weather.model.BookmarkedCity
import com.cities.weather.utils.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var mMap: GoogleMap
    var isEnable = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        var toolbar = findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        toolbar.title = AppConstants.TITLE_SAVE_CITY
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if(location!=null)
                    {
                        val latLng = LatLng(location.getLatitude(), location.getLongitude())
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
                        mMap.animateCamera(cameraUpdate)
                    }
                }
        toolbar.setNavigationOnClickListener(View.OnClickListener { finish() })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        findViewById<View>(R.id.bookmark).setOnClickListener(this)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    override fun onClick(view: View?) {
        val ButtonStar = findViewById<View>(R.id.bookmark) as ImageButton
        if (view!!.id == R.id.bookmark) {
            if (isEnable) {
                disableBookmark(ButtonStar)
            } else {
                enableBookMark(ButtonStar)
                val latLng = mMap.cameraPosition.target
                val geocoder = Geocoder(this@MapsActivity)
                try {
                    val addressList: List<Address>? =
                            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    if (addressList != null && addressList.size > 0) {
                        var address = addressList[0]
                        if (address.locality != null) {
                            val cityName: String = address.locality
                            if (!cityName.isEmpty())
                                Log.e("location", "$cityName")
                            //check if shared pref has that city
                            var savedcityList = Utils.getCity(this)
                            if (savedcityList != null) {
                                if (savedcityList.any { it.cityName == cityName }) {
                                    showToast(AppConstants.BOOKMARK_EXISTS)
                                    disableBookmark(ButtonStar)
                                } else {
                                    var bookmarkedCity: BookmarkedCity = BookmarkedCity()
                                    bookmarkedCity.cityName = cityName
                                    bookmarkedCity.latitude = latLng.latitude
                                    bookmarkedCity.longitute = latLng.longitude
                                    savedcityList.add(bookmarkedCity)
                                    Utils.saveCity(application, savedcityList)
                                    finish()
                                }
                            } else {
                                var cityList = ArrayList<BookmarkedCity>();
                                var bookmarkedCity: BookmarkedCity = BookmarkedCity()
                                bookmarkedCity.cityName = cityName
                                bookmarkedCity.latitude = latLng.latitude
                                bookmarkedCity.longitute = latLng.longitude
                                cityList.add(bookmarkedCity)
                                Utils.saveCity(application, cityList)
                                showToast(AppConstants.BOOKMARK_SAVED_SUCCESSFULLY)
                                finish()
                            }
                        } else {
                            showToast(AppConstants.UNABLE_TOGET_CITY!!)
                            disableBookmark(ButtonStar)
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            isEnable = !isEnable
        }
    }

    private fun enableBookMark(buttonStar: ImageButton) {
        buttonStar.setImageDrawable(
                ContextCompat.getDrawable(
                        applicationContext,
                        android.R.drawable.btn_star_big_on
                )
        )

    }

    private fun disableBookmark(buttonStar: ImageButton) {
        buttonStar.setImageDrawable(
                ContextCompat.getDrawable(
                        applicationContext,
                        android.R.drawable.btn_star_big_off
                )
        )

    }

    fun showToast(msg: String) {
        Toast.makeText(
                this,
                msg,
                Toast.LENGTH_SHORT
        ).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

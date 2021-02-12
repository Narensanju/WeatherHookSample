package com.cities.weather

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cities.weather.constants.AppConstants
import com.cities.weather.model.BookmarkedCity
import com.cities.weather.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : BaseActivity(), ICity {

    var permissionsList = mutableListOf<String>()
    var bookMarkFragment: BookMarkFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = AppConstants.TITLE_SAVED_CITIES
        addRequiredPermissions()
        //request location permissions if not allowed.
        if (!checkPermission()) {
            requestPermission()
        }
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (bookMarkFragment == null) {
            bookMarkFragment = BookMarkFragment()
            loadFragment(bookMarkFragment!!, AppConstants.FRAG_BOOKMARKED)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun addRequiredPermissions() {
        permissionsList.add(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun checkPermission(): Boolean {
        val isLocationEnabled: Boolean = isGranted(
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        return isLocationEnabled
    }


    fun isGranted(perm: Int): Boolean {
        return perm == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                permissionsList.toTypedArray(),
                AppConstants.ASK_MULTIPLE_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_help -> {
                startActivity(
                    Intent(this, HelpActivity::class.java)
                )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == AppConstants.ASK_MULTIPLE_PERMISSION_REQUEST_CODE) {
            if (!hasPermissions(permissionsList)) {
                Toast.makeText(this, AppConstants.PERMISSIONS_NOTGRANTED, Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun hasPermissions(permissionsList: MutableList<String>): Boolean {
        for (permission in permissionsList)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(permission)) return false
            }
        return true
    }

    override fun onCityClick(city: BookmarkedCity, viewOrDelete: Int) {
        if (viewOrDelete == AppConstants.View) {
            loadcityWeatherDetails(city)
        } else {
            bookMarkFragment!!.deleteSelectedCity(city)
        }

    }

    private fun loadcityWeatherDetails(city: BookmarkedCity) {
        loadFragment(CityDetailsFragment(this, city), AppConstants.FRAG_CITYWEATHER)
    }

    fun disablefabButton() {
        var fabBtn = findViewById<View>(R.id.fab) as FloatingActionButton
        fabBtn.visibility = View.GONE
    }


}


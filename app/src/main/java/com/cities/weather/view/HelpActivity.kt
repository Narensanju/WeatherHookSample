package com.cities.weather.view

import android.os.Bundle
import android.view.View
import com.cities.weather.R
import com.cities.weather.constants.AppConstants

class HelpActivity :BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        var toolbar = findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = AppConstants.TITLE_HELP
        toolbar.setNavigationOnClickListener(View.OnClickListener { finish() })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

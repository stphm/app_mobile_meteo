package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.weatherapp.View.CurrentWeatherFragment
import com.example.weatherapp.View.FutureWeatherFragment
import com.example.weatherapp.View.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val currentWeatherFragment = CurrentWeatherFragment()
    private val futureWeatherFragment = FutureWeatherFragment()
    private val settingsFragment = SettingsFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // by default
        replaceFragment(currentWeatherFragment)

        //Listener Fragment
        listenerFragment()
    }

    private fun listenerFragment() {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.calendar_today -> replaceFragment(currentWeatherFragment)
                R.id.calendar_weeks -> replaceFragment(futureWeatherFragment)
                R.id.settings -> replaceFragment(settingsFragment)
            }
            true
        }
    }

    // allows replace the page
    private fun replaceFragment(fragment: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }


}
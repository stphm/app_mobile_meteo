package com.example.weatherapp.View

import android.Manifest
import android.annotation.SuppressLint

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.CurrentWeatherViewModel
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import kotlinx.android.synthetic.main.current_weather_fragment.*
import retrofit2.http.GET

private const val TAG = "CurrentWeatherFragment"

class CurrentWeatherFragment : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: com.google.android.gms.location.LocationRequest

    private lateinit var viewModel: CurrentWeatherViewModel

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // localisation GPS
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
        getLastLocation()

        GET = context!!.getSharedPreferences(context?.packageName, AppCompatActivity.MODE_PRIVATE)
        SET = GET.edit()

        viewModel =  ViewModelProviders.of(this).get(CurrentWeatherViewModel::class.java)

        var cName = GET.getString("cityName", "bingöl")?.toLowerCase()
        edit_location.setText(cName)
        viewModel.refreshData(cName!!)

        getLiveData()

        refresh_layout.setOnRefreshListener {
            all_data.visibility = View.GONE
            error_text.visibility = View.GONE
            progressBar.visibility = View.GONE

            var cityName = GET.getString("cityName", cName)?.toLowerCase()
            edit_location.setText(cityName)
            viewModel.refreshData(cityName!!)
            refresh_layout.isRefreshing = false
        }

        // listener whe click icon search
        icon_searc.setOnClickListener{
            val cityName = edit_location.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewModel.refreshData(cityName)
            getLiveData()
            Log.i(TAG, "onCreate:" +cityName)
        }
    }


    private fun getLiveData() {

        // get Data in currentWeather xml
        viewModel.weather_data.observe(this, Observer {
            data ->
            data?.let {
                all_data.visibility = View.GONE

                location.text = data.name.toString()
                temp.text = data.main.temp.toString() + "°C"
                temp_min.text = "Min :" + data.main.tempMin.toString() + "°C"
                temp_max.text = "Max :" + data.main.tempMax.toString() + "°C"
                humidity.text = data.main.humidity.toString() + "%"
                wind.text = data.wind.speed.toString()

            }
        })


        // display Error
        viewModel.weather_error.observe(this, Observer {
            error ->
            error?.let {
                if (error) {
                    error_text.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    all_data.visibility = View.GONE
                } else {
                    error_text.visibility = View.GONE
                }
            }
        })

        // loading
        viewModel.weather_loading.observe(this, Observer {
            loading ->
            loading?.let {
                if (loading) {
                    progressBar.visibility = View.VISIBLE
                    error_text.visibility = View.GONE
                    all_data.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    // allow us to get the last location
    private fun getLastLocation() {
        if(checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener{
                    task ->
                    var location:Location?= task.result
                    if (location == null) {
                        getNewLocalisation()
                    } else {
                        val lat = location.latitude.toString()
                        val long = location.longitude.toString()
                        Toast.makeText(activity, "$lat $long" , Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(activity, " Merci d'activez votre localisation", Toast.LENGTH_SHORT).show()
            }

        }else {
           requestPermission()
        }
    }

    @SuppressLint( "MissingPermission")
    private fun getNewLocalisation() {
        locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1

       fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
       fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()!!)
    }

   private val locationCallback = object:LocationCallback() {
       override fun onLocationResult(p0: LocationResult) {
           var lastLocation: Location = p0.lastLocation
       }

   }

    // check the uses Permission
    private fun checkPermission():Boolean {
        if (
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    // allow us to get user permission
    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1010)
    }

    // check the location of the device is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    // built check permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1010) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
}



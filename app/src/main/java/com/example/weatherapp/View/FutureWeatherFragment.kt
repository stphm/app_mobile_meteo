package com.example.weatherapp.View

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.FutureWeatherViewModel

class FutureWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = FutureWeatherFragment()
    }

    private lateinit var viewModel: FutureWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FutureWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
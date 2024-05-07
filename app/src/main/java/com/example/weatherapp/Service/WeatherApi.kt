package com.example.weatherapp.Service

import com.example.weatherapp.Model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    // api.openweathermap.org/data/2.5/weather?units=metric&appid=5fd34a7d5dbc21e24dac7d70ad7b6004

    @GET("data/2.5/weather?units=metric&lang=fr&appid=5fd34a7d5dbc21e24dac7d70ad7b6004")
    fun gatData(
        @Query("q") cityName: String
    ): Single<WeatherModel>
}
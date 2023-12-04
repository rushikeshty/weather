package com.example.androidassignment.assignment.apiservices

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("current.json")
    fun getWeatherData(
        @Query("key")
        key: String, @Query("q") cityName: String
    ): Call<JsonObject>


}
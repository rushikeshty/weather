package com.example.androidassignment.assignment.networkservices

import com.example.androidassignment.assignment.apiservices.WeatherAPI
import com.example.androidassignment.assignment.apiservices.WeatherDataListener
import com.example.androidassignment.assignment.constant.Constant
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIService(private val listener: WeatherDataListener) {


    fun getDataFromAPI(cityName: String) {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().also {
                val weatherAPI = it.create(WeatherAPI::class.java)
                val weatherData: Call<JsonObject> =
                    weatherAPI.getWeatherData(Constant.API_KEY, cityName)

                weatherData.enqueue(object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {
                            listener.onSuccess(response.body())

                        } else {
                            listener.onFailure("Data not found")
                        }

                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        listener.onFailure("\"Oops! Something went wrong. check your internet connection\"")

                    }

                })
            }

    }

}

package com.example.androidassignment.assignment.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidassignment.assignment.apiservices.WeatherDataListener
import com.example.androidassignment.assignment.firebase.FirebaseAuthentication
import com.example.androidassignment.assignment.firebase.FirebaseListener
import com.example.androidassignment.assignment.model.DataWeather
import com.example.androidassignment.assignment.networkservices.WeatherAPIService
import com.google.gson.JsonObject

class WeatherViewModel : ViewModel() {
    var tempf: String = ""
    var tempC: String = ""
    var livedata = MutableLiveData<DataWeather>()


    private val weatherAPIService = WeatherAPIService(object : WeatherDataListener {
        override fun onSuccess(response: JsonObject?) {
            val values = response!!
            val dataWeather = DataWeather(
                values.get("location").asJsonObject.get("name").toString()
                    .replace("\"", ""),
                values.get("location").asJsonObject.get("region").toString()
                    .replace("\"", ""),
                values.get("location").asJsonObject.get("country").toString()
                    .replace("\"", ""),
                values.get("current").asJsonObject.get("temp_c").toString()
                    .plus(" \u2103").replace("\"", ""),
                values.get("current").asJsonObject.get("condition").asJsonObject.get(
                    "text"
                ).toString().replace("\"", ""),
                values.get("current").asJsonObject.get("condition").asJsonObject.get(
                    "icon"
                ).toString().replace("\"", ""),
                values.get("current").asJsonObject.get("last_updated").toString()
                    .replace("\"", ""),
                "Not raining",
                values.get("current").asJsonObject.get("wind_kph").toString()
                    .replace("\"", ""),
                values.get("current").asJsonObject.get("humidity").toString()
                    .replace("\"", ""),
                "working"
            )

            tempf = values.get("current").asJsonObject.get("temp_f").toString()
                .plus(" \u2109").replace("\"", "")
            tempC = values.get("current").asJsonObject.get("temp_c").toString()
                .plus(" \u2103").replace("\"", "")

            livedata.value = dataWeather
            Log.d("SUCCESS",dataWeather.toString())

        }


        override fun onFailure(errorMsg:String) {
            livedata.value = DataWeather("", "", "", "", "", "", "", "", "", "",
                errorMsg)
            Log.d("ERROR",errorMsg)
        }
    })


    fun getDataFromAPI(cityName: String) {
        weatherAPIService.getDataFromAPI(cityName)
    }


}
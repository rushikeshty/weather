package com.example.androidassignment.assignment.apiservices

import com.google.gson.JsonObject

interface WeatherDataListener {
    fun onSuccess(response: JsonObject?)
    fun onFailure(errorMsg:String)
}
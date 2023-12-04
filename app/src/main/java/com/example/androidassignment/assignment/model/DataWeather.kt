package com.example.androidassignment.assignment.model

data class DataWeather(
    var location: String,
    var state: String,
    var country: String,
    var temp: String,
    var weathercondition: String,
    var weatherimage: String,
    var dateTime: String,
    var rainpercentage: String,
    var windspeed: String,
    var humidity: String,
    var weathererror: String
)


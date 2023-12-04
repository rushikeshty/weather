package com.example.androidassignment.assignment.networkservices

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log


class NetworkUtils{

    fun checkInternetConnection(context:Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val capabilities = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                Log.d("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                true
            } else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                Log.d("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                true
            } else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                Log.d("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                true
            } else{
                Log.d("no internet","No internet connection")
                false
            }
        }
        return false
    }
}
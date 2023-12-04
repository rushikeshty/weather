package com.example.androidassignment.assignment.networkservices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NetworkChangeReceiver :
    BroadcastReceiver() {

    private val networkUtils = NetworkUtils()

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    override fun onReceive(context: Context, p1: Intent?) {
        networkUtils.checkInternetConnection(context)
    }


}
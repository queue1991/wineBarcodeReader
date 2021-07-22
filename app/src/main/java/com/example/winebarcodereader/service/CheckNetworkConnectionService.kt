package com.example.winebarcodereader.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class CheckNetworkConnectionService(context: Context, private var networkCallback:ConnectivityManager.NetworkCallback) {

    private var networkRequest: NetworkRequest? = null
    private var connectivityManager : ConnectivityManager? = null

    init {
        this.networkRequest =
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()

        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }

    fun networkStateRegister() {
        connectivityManager!!.registerNetworkCallback(networkRequest!!, networkCallback)
    }

    fun networkStateUnregister() {
        connectivityManager!!.unregisterNetworkCallback(networkCallback)
    }
}
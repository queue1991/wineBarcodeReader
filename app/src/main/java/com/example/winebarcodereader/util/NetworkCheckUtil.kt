package com.example.winebarcodereader.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log

object NetworkCheckUtil{

    /**
     * CheckNetworkConnectionUtil에서는 바뀌는 순간에서만 체크 가능하기때문에 만든 function
     *
     */
    fun isNetworkConnected(context:Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.d("dateUtil", "getTodayDateByYYYYMMDD :: TRANSPORT_WIFI")
                    return true
                }
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.d("dateUtil", "getTodayDateByYYYYMMDD :: TRANSPORT_CELLULAR")
                    true
                }
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.d("dateUtil", "getTodayDateByYYYYMMDD :: TRANSPORT_ETHERNET")
                    true
                }
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }
}
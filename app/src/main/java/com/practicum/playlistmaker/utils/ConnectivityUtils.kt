package com.practicum.playlistmaker.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun Context.isConnected(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
    capabilities?.let {
        when{
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        }
    }
    return false
}
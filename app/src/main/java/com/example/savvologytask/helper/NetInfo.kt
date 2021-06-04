package com.example.savvologytask.helper

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


object NetInfo : LiveData<Boolean>(), KoinComponent {
    /*private var broadcastReceiver: BroadcastReceiver? = null
    private val application: Application by inject()*/

    private val connectivityManager: ConnectivityManager by inject()

    /* fun isInternetOn(): Boolean {
         val activeNetwork = connectivityManager.activeNetworkInfo
         return activeNetwork != null && activeNetwork.isConnectedOrConnecting
     }*/

    fun isInternetOn(): Boolean {
        val result: Boolean

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ) -> true
                else -> false
            }
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            result = when {
                activeNetwork != null && activeNetwork.isConnectedOrConnecting -> true
                else -> false
            }
        }

        return result
    }
}
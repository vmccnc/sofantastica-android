package com.furniture.duet.background

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import com.furniture.duet.domain.exceptions.IsNotOnlineException
import javax.inject.Inject

open class InternetConnectionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun isOnline() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: throw IsNotOnlineException()

        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: throw IsNotOnlineException()

        if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_SATELLITE) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return
        }
        throw IsNotOnlineException()
    }
}
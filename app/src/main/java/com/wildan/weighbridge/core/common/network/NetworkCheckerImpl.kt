package com.wildan.weighbridge.core.common.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

/**
 * Created by Wildan Nafian on 24/08/2022.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */
class NetworkCheckerImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager,
) : NetworkChecker {

    override suspend fun isConnected(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return if (capabilities != null) {
            val isConnectedToCellPhone = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            val isConnectedToWifi = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            isConnectedToCellPhone || isConnectedToWifi
        } else false
    }
}

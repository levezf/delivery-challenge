package br.com.levez.challenge.delivery.network.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import br.com.levez.challenge.delivery.extension.connectivityManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkManager private constructor(
    private val context: Context,
) : INetworkManager {

    companion object {
        @Volatile
        private var INSTANCE: INetworkManager? = null

        fun getInstance(context: Context): INetworkManager =
            INSTANCE ?: synchronized(this) {
                val instance = NetworkManager(context)
                INSTANCE = instance
                instance
            }
    }

    @Suppress("DEPRECATION")
    override fun isOnline(): Boolean = runCatching {
        with(context.connectivityManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getNetworkCapabilities(activeNetwork) != null
            } else {
                activeNetworkInfo?.isConnectedOrConnecting == true &&
                    activeNetworkInfo?.isAvailable == true
            }
        }
    }.getOrDefault(false)

    override fun connectionState(): Flow<ConnectionState> = callbackFlow {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySendBlocking(ConnectionState.CONNECTED)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySendBlocking(ConnectionState.NOT_CONNECTED)
            }
        }
        trySendBlocking(
            if (isOnline()) {
                ConnectionState.CONNECTED
            } else {
                ConnectionState.NOT_CONNECTED
            }
        )
        context.connectivityManager.registerNetworkCallback(networkRequest, callback)
        awaitClose { context.connectivityManager.unregisterNetworkCallback(callback) }
    }
}

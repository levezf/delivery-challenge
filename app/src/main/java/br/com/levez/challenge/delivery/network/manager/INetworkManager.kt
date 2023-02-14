package br.com.levez.challenge.delivery.network.manager

import kotlinx.coroutines.flow.Flow

interface INetworkManager {
    fun isOnline(): Boolean
    fun connectionState(): Flow<ConnectionState>
}

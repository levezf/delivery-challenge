package br.com.levez.challenge.delivery.ui.connection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import br.com.levez.challenge.delivery.network.manager.INetworkManager

class NoConnectionViewModel(
    private val networkManager: INetworkManager,
) : ViewModel() {
    val internetConnectionState = networkManager.connectionState().asLiveData()
}

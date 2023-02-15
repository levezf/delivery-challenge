package br.com.levez.challenge.delivery.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.levez.challenge.delivery.model.DeliveryMinimal
import br.com.levez.challenge.delivery.repository.DeliveryRepository
import br.com.levez.challenge.delivery.ui.common.livedata.SingleLiveEvent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DeliveryListViewModel(
    private val repository: DeliveryRepository,
) : ViewModel() {
    companion object {
        private const val TIMEOUT_IN_MILLIS = 5_000L
    }

    private val _openDeliveryRegistration = SingleLiveEvent<Long?>()
    val openDeliveryRegistration: SingleLiveEvent<Long?>
        get() = _openDeliveryRegistration

    val uiState: StateFlow<ListDeliveryUiState> =
        repository.allMinimalDeliveries.map(this::mapDeliveryListUiState).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_IN_MILLIS),
            initialValue = ListDeliveryUiState.Loading
        )

    fun openDeliveryRegistration() {
        _openDeliveryRegistration.call()
    }

    private fun mapDeliveryListUiState(customers: List<DeliveryMinimal>): ListDeliveryUiState =
        when {
            customers.isEmpty() -> ListDeliveryUiState.Empty
            else -> ListDeliveryUiState.Loaded(customers)
        }
}

sealed interface ListDeliveryUiState {
    object Loading : ListDeliveryUiState

    data class Loaded(val deliveries: List<DeliveryMinimal>) : ListDeliveryUiState

    object Empty : ListDeliveryUiState
}

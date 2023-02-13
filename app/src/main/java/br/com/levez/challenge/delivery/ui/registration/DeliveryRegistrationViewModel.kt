package br.com.levez.challenge.delivery.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.levez.challenge.delivery.R
import br.com.levez.challenge.delivery.exception.DeliveryException
import br.com.levez.challenge.delivery.extension.sanitize
import br.com.levez.challenge.delivery.model.City
import br.com.levez.challenge.delivery.model.Delivery
import br.com.levez.challenge.delivery.network.manager.INetworkManager
import br.com.levez.challenge.delivery.repository.DeliveryRepository
import br.com.levez.challenge.delivery.repository.LocalityRepository
import br.com.levez.challenge.delivery.validator.DeliveryValidator
import kotlinx.coroutines.launch

class DeliveryRegistrationViewModel(
    private val deliveryRepository: DeliveryRepository,
    private val localityRepository: LocalityRepository,
    private val networkManager: INetworkManager,
) : ViewModel() {
    val externalId: MutableLiveData<String?> = MutableLiveData(null)
    val numberOfPackages: MutableLiveData<String?> = MutableLiveData(null)
    val deadline: MutableLiveData<String?> = MutableLiveData(null)
    val customerName: MutableLiveData<String?> = MutableLiveData(null)
    val customerCpf: MutableLiveData<String?> = MutableLiveData(null)
    val addressZipCode: MutableLiveData<String?> = MutableLiveData(null)
    val addressState: MutableLiveData<String?> = MutableLiveData(null)
    val addressCity: MutableLiveData<String?> = MutableLiveData(null)
    val addressNeighborhood: MutableLiveData<String?> = MutableLiveData(null)
    val addressStreet: MutableLiveData<String?> = MutableLiveData(null)
    val addressNumber: MutableLiveData<String?> = MutableLiveData(null)
    val addressComplement: MutableLiveData<String?> = MutableLiveData(null)

    val internetConnectionState = networkManager.connectionState().asLiveData()

    val availableCities: LiveData<List<City>?> = Transformations.switchMap(addressState) { state ->
        liveData {
            if (state.isNullOrBlank()) {
                emit(null)
            } else {
                emit(localityRepository.getCitiesByState(state))
            }
        }
    }.distinctUntilChanged()

    private val _failure = MutableLiveData<Int?>(null)
    val failure: LiveData<Int?>
        get() = _failure

    private val _uiState: MutableLiveData<DeliveryRegistrationUiState> =
        MutableLiveData(DeliveryRegistrationUiState.Editing)
    val uiState: LiveData<DeliveryRegistrationUiState>
        get() = _uiState

    val isLayoutEditable: LiveData<Boolean>
        get() = Transformations.map(_uiState) { state ->
            state == DeliveryRegistrationUiState.Editing
        }

    init {
        availableCities.observeForever {
            addressCity.value = null
        }
    }

    fun validateAndRegisterDelivery() {
        viewModelScope.launch {
            _uiState.value = DeliveryRegistrationUiState.Registering

            val delivery = Delivery(
                externalId.value.sanitize(),
                numberOfPackages.value.sanitize(),
                deadline.value.sanitize(),
                customerName.value.sanitize(),
                customerCpf.value.sanitize(),
                addressZipCode.value.sanitize(),
                addressState.value.sanitize(),
                addressCity.value.sanitize(),
                addressNeighborhood.value.sanitize(),
                addressStreet.value.sanitize(),
                addressNumber.value.sanitize(),
                addressComplement.value.sanitize(),
            )

            try {
                DeliveryValidator.validate(delivery)
            } catch (e: DeliveryException) {
                _failure.value = e.messageId
                _uiState.value = DeliveryRegistrationUiState.Editing
                return@launch
            }

            if (deliveryRepository.existsExternalId(delivery.externalId)) {
                _failure.value = R.string.error_external_id_already_registered
                _uiState.value = DeliveryRegistrationUiState.Editing
                return@launch
            }

            if (deliveryRepository.insertOrUpdate(delivery).not()) {
                _failure.value = R.string.error_generic_registration_delivery
                _uiState.value = DeliveryRegistrationUiState.Editing
                return@launch
            }

            _uiState.value = DeliveryRegistrationUiState.Registered
        }
    }
}

sealed interface DeliveryRegistrationUiState {
    object Registering : DeliveryRegistrationUiState
    object Editing : DeliveryRegistrationUiState
    object Registered : DeliveryRegistrationUiState
    object NoInternetConnection : DeliveryRegistrationUiState
}

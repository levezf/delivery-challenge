package br.com.levez.challenge.delivery.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
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
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    companion object {
        private const val SAVED_EXTERNAL_ID = "external_id"
        private const val SAVED_NUMBER_OF_PACKAGES = "number_of_packages"
        private const val SAVED_DEADLINE = "deadline"
        private const val SAVED_CUSTOMER_NAME = "customer_name"
        private const val SAVED_CUSTOMER_CPF = "customer_cpf"
        private const val SAVED_ADDRESS_ZIP_CODE = "address_zip_code"
        private const val SAVED_ADDRESS_STATE = "address_state"
        private const val SAVED_ADDRESS_CITY = "address_city"
        private const val SAVED_ADDRESS_NEIGHBORHOOD = "address_neighborhood"
        private const val SAVED_ADDRESS_STREET = "address_street"
        private const val SAVED_ADDRESS_NUMBER = "address_number"
        private const val SAVED_ADDRESS_COMPLEMENT = "address_complement"
    }

    val externalId: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_EXTERNAL_ID, null)

    val numberOfPackages: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_NUMBER_OF_PACKAGES, null)

    val deadline: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_DEADLINE, null)

    val customerName: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_CUSTOMER_NAME, null)

    val customerCpf: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_CUSTOMER_CPF, null)

    val addressZipCode: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_ADDRESS_ZIP_CODE, null)

    val addressState: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_ADDRESS_STATE, null)

    val addressCity: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_ADDRESS_CITY, null)

    val addressNeighborhood: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_ADDRESS_NEIGHBORHOOD, null)

    val addressStreet: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_ADDRESS_STREET, null)

    val addressNumber: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_ADDRESS_NUMBER, null)

    val addressComplement: MutableLiveData<String?> =
        savedStateHandle.getLiveData(SAVED_ADDRESS_COMPLEMENT, null)

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
        savedStateHandle.getLiveData("", DeliveryRegistrationUiState.Editing)
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

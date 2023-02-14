package br.com.levez.challenge.delivery.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
        private const val SAVED_ID_DELIVERY_EDITING = "id_delivery_editing"
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

    private val _availableCities = MutableLiveData<List<City>?>(null)
    val availableCities: LiveData<List<City>?>
        get() = _availableCities

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

    val isDeliveryIdEditable: LiveData<Boolean>
        get() = Transformations.map(isLayoutEditable) { layoutEditable ->
            layoutEditable && idDelivery == null
        }

    var idDelivery: Long? = savedStateHandle[SAVED_ID_DELIVERY_EDITING]
        set(value) {
            field = value
            fillFields(value)
        }

    private fun fillFields(idDelivery: Long?) {
        idDelivery?.let { id ->
            viewModelScope.launch {
                _uiState.value = DeliveryRegistrationUiState.Loading

                val delivery = deliveryRepository.getDeliveryById(id)
                delivery?.let {
                    externalId.value = it.externalId
                    numberOfPackages.value = it.numberOfPackages
                    deadline.value = it.deadline
                    customerName.value = it.customerName
                    customerCpf.value = it.customerCpf
                    addressZipCode.value = it.addressZipCode
                    addressState.value = it.addressState
                    addressCity.value = it.addressCity
                    addressNeighborhood.value = it.addressNeighborhood
                    addressStreet.value = it.addressStreet
                    addressNumber.value = it.addressNumber
                    addressComplement.value = it.addressComplement
                }

                refreshCity(false)

                _uiState.value = DeliveryRegistrationUiState.Editing
            }
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
                idDelivery,
            )

            try {
                DeliveryValidator.validate(delivery)
            } catch (e: DeliveryException) {
                _failure.value = e.messageId
                _uiState.value = DeliveryRegistrationUiState.Editing
                return@launch
            }

            if (idDelivery == null && deliveryRepository.existsExternalId(delivery.externalId)) {
                _failure.value = R.string.error_external_id_already_registered
                _uiState.value = DeliveryRegistrationUiState.Editing
                return@launch
            }

            if (deliveryRepository.insertOrUpdate(delivery).not()) {
                _failure.value = R.string.error_generic_registration_delivery
                _uiState.value = DeliveryRegistrationUiState.Editing
                return@launch
            }

            _uiState.value = DeliveryRegistrationUiState.Finished
        }
    }

    fun refreshCity(clearCity: Boolean = true) {
        viewModelScope.launch {
            _uiState.value = DeliveryRegistrationUiState.Loading

            if (clearCity) {
                addressCity.value = null
            }

            _availableCities.value = addressState.value?.let { state ->
                localityRepository.getCitiesByState(state)
            }

            _uiState.value = DeliveryRegistrationUiState.Editing
        }
    }

    fun deleteDelivery() {
        viewModelScope.launch {
            _uiState.value = DeliveryRegistrationUiState.Loading

            idDelivery?.let { id ->
                deliveryRepository.deleteDeliveryById(id)
                _uiState.value = DeliveryRegistrationUiState.Finished
            } ?: run {
                _failure.value = R.string.error_cannot_delete_delivery
                _uiState.value = DeliveryRegistrationUiState.Editing
            }
        }
    }
}

sealed interface DeliveryRegistrationUiState {
    object Loading : DeliveryRegistrationUiState
    object Registering : DeliveryRegistrationUiState
    object Editing : DeliveryRegistrationUiState
    object Finished : DeliveryRegistrationUiState
    object NoInternetConnection : DeliveryRegistrationUiState
}

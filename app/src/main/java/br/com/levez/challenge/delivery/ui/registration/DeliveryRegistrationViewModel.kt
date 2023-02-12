package br.com.levez.challenge.delivery.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.levez.challenge.delivery.R
import br.com.levez.challenge.delivery.model.Delivery
import br.com.levez.challenge.delivery.repository.DeliveryRepository
import kotlinx.coroutines.launch

class DeliveryRegistrationViewModel(
    private val deliveryRepository: DeliveryRepository,
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

    fun registerDelivery() {
        viewModelScope.launch {
            _uiState.value = DeliveryRegistrationUiState.Registering

            val delivery = Delivery(
                externalId.value.orEmpty().trim(),
                numberOfPackages.value.orEmpty().trim(),
                deadline.value.orEmpty().trim(),
                customerName.value.orEmpty().trim(),
                customerCpf.value.orEmpty().trim(),
                addressZipCode.value.orEmpty().trim(),
                addressState.value.orEmpty().trim(),
                addressCity.value.orEmpty().trim(),
                addressNeighborhood.value.orEmpty().trim(),
                addressStreet.value.orEmpty().trim(),
                addressNumber.value.orEmpty().trim(),
                addressComplement.value.orEmpty().trim(),
            )

            if (delivery.isInvalid()) {
                _failure.value = R.string.error_required_fields
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
}

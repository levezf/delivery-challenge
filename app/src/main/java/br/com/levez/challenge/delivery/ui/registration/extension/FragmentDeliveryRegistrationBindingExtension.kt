package br.com.levez.challenge.delivery.ui.registration.extension

import android.widget.AdapterView
import br.com.levez.challenge.delivery.databinding.FragmentDeliveryRegistrationBinding
import br.com.levez.challenge.delivery.ui.registration.DeliveryRegistrationViewModel
import br.com.levez.challenge.delivery.ui.registration.mask.SimpleMaskTextWatcher

fun FragmentDeliveryRegistrationBinding.initializeListeners(
    viewModel: DeliveryRegistrationViewModel,
) {
    autoCompleteState.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
        viewModel.refreshCity()
    }
    includeBottomButton.bottomActionButton.setOnClickListener {
        viewModel.validateAndRegisterDelivery()
    }
}

fun FragmentDeliveryRegistrationBinding.initializeMasks() {
    editTextDeadline.addTextChangedListener(
        SimpleMaskTextWatcher.date(editTextDeadline)
    )
    editTextZipCode.addTextChangedListener(
        SimpleMaskTextWatcher.cep(editTextZipCode)
    )
    editTextCustomerCpf.addTextChangedListener(
        SimpleMaskTextWatcher.cpf(editTextCustomerCpf)
    )
}

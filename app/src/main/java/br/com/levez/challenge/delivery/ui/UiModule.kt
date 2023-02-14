package br.com.levez.challenge.delivery.ui

import br.com.levez.challenge.delivery.ui.connection.NoConnectionViewModel
import br.com.levez.challenge.delivery.ui.registration.DeliveryRegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val deliveryRegistrationModule = module {
    viewModelOf(::DeliveryRegistrationViewModel)
}

val noConnectionModule = module {
    viewModelOf(::NoConnectionViewModel)
}

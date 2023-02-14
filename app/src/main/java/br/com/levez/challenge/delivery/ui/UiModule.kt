package br.com.levez.challenge.delivery.ui

import br.com.levez.challenge.delivery.ui.connection.NoConnectionViewModel
import br.com.levez.challenge.delivery.ui.list.DeliveryListViewModel
import br.com.levez.challenge.delivery.ui.registration.DeliveryRegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val deliveryListModule = module {
    viewModelOf(::DeliveryListViewModel)
}

val deliveryRegistrationModule = module {
    viewModelOf(::DeliveryRegistrationViewModel)
}

val noConnectionModule = module {
    viewModelOf(::NoConnectionViewModel)
}

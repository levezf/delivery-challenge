package br.com.levez.challenge.delivery.module

import br.com.levez.challenge.delivery.repository.DeliveryRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { DeliveryRepository(get()) }
}

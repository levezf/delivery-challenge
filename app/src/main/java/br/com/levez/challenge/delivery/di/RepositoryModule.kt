package br.com.levez.challenge.delivery.di

import br.com.levez.challenge.delivery.repository.DeliveryRepository
import br.com.levez.challenge.delivery.repository.LocalityRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { DeliveryRepository(get()) }
    single { LocalityRepository(get()) }
}

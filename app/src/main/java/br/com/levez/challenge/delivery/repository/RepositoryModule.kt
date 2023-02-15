package br.com.levez.challenge.delivery.repository

import org.koin.dsl.module

val repositoryModule = module {
    single { DeliveryRepository(get()) }
    single { LocalityRepository(get()) }
}

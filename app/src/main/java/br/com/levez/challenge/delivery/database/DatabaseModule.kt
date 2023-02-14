package br.com.levez.challenge.delivery.database

import br.com.levez.challenge.delivery.database.dao.DeliveryDao
import org.koin.dsl.module

val databaseModule = module {
    single { DeliveryExpressDatabase.getDatabase(get()) }
    single { provideDeliveryDao(get()) }
}

fun provideDeliveryDao(database: DeliveryExpressDatabase): DeliveryDao = database.deliveryDao()

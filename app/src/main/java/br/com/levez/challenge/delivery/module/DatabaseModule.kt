package br.com.levez.challenge.delivery.module

import br.com.levez.challenge.delivery.database.DeliveryExpressDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { DeliveryExpressDatabase.getDatabase(get()).deliveryDao() }
}

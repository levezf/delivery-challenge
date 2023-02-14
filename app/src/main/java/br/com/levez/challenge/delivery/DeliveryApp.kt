package br.com.levez.challenge.delivery

import android.app.Application
import br.com.levez.challenge.delivery.database.databaseModule
import br.com.levez.challenge.delivery.network.networkModule
import br.com.levez.challenge.delivery.repository.repositoryModule
import br.com.levez.challenge.delivery.ui.deliveryRegistrationModule
import br.com.levez.challenge.delivery.ui.noConnectionModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DeliveryApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initializeDependencyInjection()
    }

    private fun initializeDependencyInjection() {
        startKoin {
            androidContext(this@DeliveryApp)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                deliveryRegistrationModule,
                noConnectionModule,
            )
        }
    }
}

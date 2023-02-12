package br.com.levez.challenge.delivery

import android.app.Application
import br.com.levez.challenge.delivery.module.databaseModule
import br.com.levez.challenge.delivery.module.deliveryRegistrationModule
import br.com.levez.challenge.delivery.module.repositoryModule
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
                repositoryModule,
                deliveryRegistrationModule,
            )
        }
    }
}
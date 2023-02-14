package br.com.levez.challenge.delivery.repository

import br.com.levez.challenge.delivery.database.dao.DeliveryDao
import br.com.levez.challenge.delivery.model.Delivery
import br.com.levez.challenge.delivery.model.DeliveryMinimal
import kotlinx.coroutines.flow.Flow

class DeliveryRepository(private val deliveryDao: DeliveryDao) {

    val allMinimalDeliveries: Flow<List<DeliveryMinimal>> = deliveryDao.getAllMinimal()

    suspend fun insertOrUpdate(delivery: Delivery): Boolean =
        deliveryDao.insertOrUpdate(delivery) >= 0

    suspend fun existsExternalId(externalId: String): Boolean =
        deliveryDao.existsExternalId(externalId) > 0

    suspend fun getDeliveryById(id: Long): Delivery? = deliveryDao.getById(id)
}

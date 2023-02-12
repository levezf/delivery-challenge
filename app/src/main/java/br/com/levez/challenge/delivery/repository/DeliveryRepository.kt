package br.com.levez.challenge.delivery.repository

import br.com.levez.challenge.delivery.database.dao.DeliveryDao
import br.com.levez.challenge.delivery.model.Delivery

class DeliveryRepository(private val deliveryDao: DeliveryDao) {

    suspend fun insertOrUpdate(delivery: Delivery) {
        deliveryDao.insertOrUpdate(delivery)
    }
}

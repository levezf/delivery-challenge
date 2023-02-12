package br.com.levez.challenge.delivery.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import br.com.levez.challenge.delivery.model.Delivery

@Dao
interface DeliveryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrUpdate(delivery: Delivery): Long
}

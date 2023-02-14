package br.com.levez.challenge.delivery.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.levez.challenge.delivery.model.Delivery

@Dao
interface DeliveryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrUpdate(delivery: Delivery): Long

    @Query("SELECT COUNT(1) FROM deliveries WHERE external_id = :externalId")
    suspend fun existsExternalId(externalId: String): Long
}

package br.com.levez.challenge.delivery.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.levez.challenge.delivery.model.Delivery
import br.com.levez.challenge.delivery.model.DeliveryMinimal
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(delivery: Delivery): Long

    @Query("SELECT COUNT(1) FROM deliveries WHERE external_id = :externalId")
    suspend fun existsExternalId(externalId: String): Long

    @Query("SELECT * FROM DeliveryMinimal ORDER BY id")
    fun getAllMinimal(): Flow<List<DeliveryMinimal>>

    @Query("SELECT * FROM deliveries WHERE id = :id")
    suspend fun getById(id: Long): Delivery?
}

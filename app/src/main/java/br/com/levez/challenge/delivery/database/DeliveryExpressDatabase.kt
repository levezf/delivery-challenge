package br.com.levez.challenge.delivery.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.levez.challenge.delivery.database.dao.DeliveryDao
import br.com.levez.challenge.delivery.model.Delivery

@Database(
    entities = [Delivery::class],
    version = 1,
    exportSchema = false,
)
abstract class DeliveryExpressDatabase : RoomDatabase() {

    abstract fun deliveryDao(): DeliveryDao

    companion object {
        private const val DATABASE_NAME = "delivery_database"

        @Volatile
        private var INSTANCE: DeliveryExpressDatabase? = null

        fun getDatabase(context: Context): DeliveryExpressDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DeliveryExpressDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
    }
}

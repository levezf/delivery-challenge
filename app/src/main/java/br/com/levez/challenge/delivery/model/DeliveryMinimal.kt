package br.com.levez.challenge.delivery.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    """
        SELECT 
            id, 
            external_id, 
            deadline,
            address_street || ", " ||  address_number || 
                   CASE WHEN (address_complement is null  OR address_complement = "") THEN  "" ELSE ", " || address_complement  END  || 
                   " - " || address_neighborhood || ". " ||  address_zip_code || ". "  || address_city ||  "/" || address_state AS address
        FROM deliveries
    """
)
data class DeliveryMinimal(
    @ColumnInfo("external_id")
    val externalId: String,
    @ColumnInfo("deadline")
    val deadline: String,
    @ColumnInfo("address")
    val address: String,
    val id: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeliveryMinimal

        if (externalId != other.externalId) return false
        if (deadline != other.deadline) return false
        if (address != other.address) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = externalId.hashCode()
        result = 31 * result + deadline.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}

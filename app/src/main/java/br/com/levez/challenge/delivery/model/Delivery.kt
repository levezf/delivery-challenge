package br.com.levez.challenge.delivery.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "deliveries",
    indices = [Index(value = ["id", "external_id"], unique = true)]
)
data class Delivery(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo("external_id")
    val externalId: String,
    @ColumnInfo("number_of_packages")
    val numberOfPackages: String,
    @ColumnInfo("deadline")
    val deadline: String,
    @ColumnInfo("customer_name")
    val customerName: String,
    @ColumnInfo("customer_cpf")
    val customerCpf: String,
    @ColumnInfo("address_zip_code")
    val addressZipCode: String,
    @ColumnInfo("address_state")
    val addressState: String,
    @ColumnInfo("address_city")
    val addressCity: String,
    @ColumnInfo("address_neighborhood")
    val addressNeighborhood: String,
    @ColumnInfo("address_street")
    val addressStreet: String,
    @ColumnInfo("address_number")
    val addressNumber: String,
    @ColumnInfo("address_complement")
    val addressComplement: String?,
) {
    fun isValid() = externalId.isNotBlank() &&
            numberOfPackages.isNotBlank() &&
            deadline.isNotBlank() &&
            customerName.isNotBlank() &&
            customerCpf.isNotBlank() &&
            addressZipCode.isNotBlank() &&
            addressState.isNotBlank() &&
            addressCity.isNotBlank() &&
            addressNeighborhood.isNotBlank() &&
            addressStreet.isNotBlank() &&
            addressNumber.isNotBlank()

    fun isNewUser() = id == null
}

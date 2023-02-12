package br.com.levez.challenge.delivery.model

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    val id: String,
    @SerializedName("nome")
    val name: String,
)

package br.com.levez.challenge.delivery.network

import br.com.levez.challenge.delivery.model.City
import retrofit2.http.GET
import retrofit2.http.Path

interface LocalityApi {

    @GET("estados/{state}/municipios")
    suspend fun getCityByState(@Path("state") state: String): List<City>
}
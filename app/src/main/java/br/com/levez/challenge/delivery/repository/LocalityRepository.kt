package br.com.levez.challenge.delivery.repository

import br.com.levez.challenge.delivery.model.City
import br.com.levez.challenge.delivery.network.api.LocalityApi

class LocalityRepository(
    private val localityApi: LocalityApi,
) {
    suspend fun getCitiesByState(state: String): List<City> = runCatching {
        localityApi.getCityByState(state)
    }.getOrDefault(emptyList())
}

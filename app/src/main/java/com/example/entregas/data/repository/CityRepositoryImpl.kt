package com.example.entregas.data.repository

import com.example.entregas.data.remote.CityService
import com.example.entregas.domain.repository.CityRepository

class CityRepositoryImpl (
    private val service: CityService
) : CityRepository {

    override suspend fun getCityUf(uf: String): List<String> {
        return service.getCidadesPorUf(uf).map { it.nome }
    }
}
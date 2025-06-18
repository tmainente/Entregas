package com.example.entregas.data.repository

import com.example.entregas.data.remote.CityService
import com.example.entregas.domain.repository.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CityRepositoryImpl (
    private val service: CityService
) : CityRepository {

    override suspend fun getCityUf(uf: String): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val city = service.getCidadesPorUf(uf).map { it.nome }
                Result.success(city)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
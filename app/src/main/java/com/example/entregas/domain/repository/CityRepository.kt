package com.example.entregas.domain.repository

interface CityRepository {
    suspend fun getCityUf(uf: String): List<String>
}
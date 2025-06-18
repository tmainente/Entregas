package com.example.entregas.domain.usecases

import com.example.entregas.domain.repository.CityRepository

class ShowCityUseCase (private val repository: CityRepository) {
    suspend operator fun invoke(uf: String): List<String> = repository.getCityUf(uf)
}
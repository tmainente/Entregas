package com.example.entregas.domain.usecases

import com.example.entregas.domain.repository.DeliveryRepository
import com.example.entregas.domain.model.Delivery
import kotlinx.coroutines.flow.Flow

class ShowDeliveryUseCase (private val repository: DeliveryRepository) {
    operator fun invoke(): Flow<List<Delivery>> = repository.getAlldeliveries()
}
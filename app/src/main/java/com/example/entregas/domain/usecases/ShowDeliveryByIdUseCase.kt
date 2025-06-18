package com.example.entregas.domain.usecases

import com.example.entregas.domain.repository.DeliveryRepository
import com.example.entregas.domain.model.Delivery
import kotlinx.coroutines.flow.Flow

class ShowDeliveryByIdUseCase (private val repository: DeliveryRepository) {
    suspend operator fun invoke(id: Long): Delivery? {
        return repository.getDeliveryById(id)
    }}
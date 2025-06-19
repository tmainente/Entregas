package com.example.entregas.domain.usecases

import com.example.entregas.domain.repository.DeliveryRepository
import com.example.entregas.domain.model.Delivery

class UpdateDeliveryUseCase (private val repository: DeliveryRepository) {
    suspend operator fun invoke(delivery: Delivery) : Result<Unit> = repository.updateDelivery(delivery)
}
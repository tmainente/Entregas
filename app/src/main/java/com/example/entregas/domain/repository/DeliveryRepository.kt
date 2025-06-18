package com.example.entregas.domain.repository

import com.example.entregas.domain.model.Delivery
import kotlinx.coroutines.flow.Flow

interface DeliveryRepository {
    suspend fun insertDelivery(delivery: Delivery) : Result<Unit>
    suspend fun updateDelivery(delivery: Delivery) : Result<Unit>
    suspend fun deleteDelivery(delivery: Delivery) : Result<Unit>
    fun getAlldeliveries(): Result<Flow<List<Delivery>>>
}
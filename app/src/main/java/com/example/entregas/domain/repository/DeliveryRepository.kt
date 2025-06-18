package com.example.entregas.domain.repository

import com.example.entregas.domain.model.Delivery
import kotlinx.coroutines.flow.Flow

interface DeliveryRepository {
    suspend fun insertDelivery(delivery: Delivery)
    suspend fun updateDelivery(delivery: Delivery)
    suspend fun deleteDelivery(delivery: Delivery)
    fun getAlldeliveries(): Flow<List<Delivery>>
}
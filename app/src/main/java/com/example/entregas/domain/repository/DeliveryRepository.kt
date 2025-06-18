package com.example.entregas.domain.repository

import com.example.entregas.domain.model.Delivery
import kotlinx.coroutines.flow.Flow

interface DeliveryRepository {
    suspend fun insert(delivery: Delivery)
    suspend fun update(delivery: Delivery)
    suspend fun delete(delivery: Delivery)
    fun getAlldeliveries(): Flow<List<Delivery>>
}
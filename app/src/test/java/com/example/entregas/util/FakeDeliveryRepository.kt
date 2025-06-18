package com.example.entregas.util


import com.example.entregas.domain.model.Delivery
import com.example.entregas.domain.repository.DeliveryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeDeliveryRepository : DeliveryRepository {

    private val deliveries = MutableStateFlow<List<Delivery>>(emptyList())

    override suspend fun insertDelivery(delivery: Delivery) {
        deliveries.update { current -> current + delivery }
    }

    override suspend fun updateDelivery(delivery: Delivery) {
        deliveries.update { current ->
            current.map { if (it.id == delivery.id) delivery else it }
        }
    }

    override suspend fun deleteDelivery(delivery: Delivery) {
        deliveries.update { current -> current.filter { it.id != delivery.id } }
    }

    override fun getAlldeliveries(): Flow<List<Delivery>> = deliveries


}
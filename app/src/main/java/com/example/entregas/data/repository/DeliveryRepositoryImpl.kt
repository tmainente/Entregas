package com.example.entregas.data.repository

import com.example.entregas.domain.repository.DeliveryRepository
import com.example.entregas.data.local.DeliveryDao
import com.example.entregas.data.local.DeliveryEntity
import com.example.entregas.domain.model.Delivery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeliveryRepositoryImpl (
    private val dao: DeliveryDao
) : DeliveryRepository {

    override suspend fun insertDelivery(delivery: Delivery) :Result<Unit> = runCatching {
        dao.insert(delivery.toEntity())
    }

    override suspend fun updateDelivery(delivery: Delivery) : Result<Unit> = runCatching {
        dao.update(delivery.toEntity())
    }

    override suspend fun deleteDelivery(delivery: Delivery) : Result<Unit> = runCatching {
        dao.delete(delivery.toEntity())
    }

    override fun getAlldeliveries(): Result<Flow<List<Delivery>>> = runCatching {
        dao.getAll().map { list -> list.map { it.toModel() } }
    }

    private fun Delivery.toEntity() = DeliveryEntity(
        id, quantPackage, dateLimit, nameClient, cpfClient, cep,
        uf, city, neighborhood, street, number, complement
    )

    private fun DeliveryEntity.toModel() = Delivery(
        id, quantPackage, dateLimit, nameClient, cpfClient, cep,
        uf, city, neighborhood, street, number, complement
    )
}
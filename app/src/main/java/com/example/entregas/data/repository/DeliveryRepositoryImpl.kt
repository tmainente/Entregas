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

    override suspend fun insert(delivery: Delivery) {
        dao.insert(delivery.toEntity())
    }

    override suspend fun update(delivery: Delivery) {
        dao.update(delivery.toEntity())
    }

    override suspend fun delete(delivery: Delivery) {
        dao.delete(delivery.toEntity())
    }

    override fun getAlldeliveries(): Flow<List<Delivery>> =
        dao.getAll().map { list -> list.map { it.toModel() } }

    override suspend fun getDeliveryById(id: Long): Delivery? {
        return dao.getById(id)
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
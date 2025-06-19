package com.example.entregas.data.repository

import com.example.entregas.data.local.DeliveryDao
import com.example.entregas.data.local.DeliveryEntity
import com.example.entregas.data.toEntity
import com.example.entregas.data.toModel
import com.example.entregas.domain.model.Delivery
import com.example.entregas.domain.repository.DeliveryRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class DeliveryRepositoryImplTest {

    private val dao: DeliveryDao = mockk(relaxed = true)
    private lateinit var repository: DeliveryRepository
    private val mockDelivery = DeliveryEntity(
        id = 1,
        quantPackage = 3,
        dateLimit = "30/06/96",
        nameClient = "Maria",
        cpfClient = "12345678901",
        cep = "12345-678",
        uf = "SP",
        city = "São Paulo",
        neighborhood = "Centro",
        street = "Rua A",
        number = "123",
        complement = "aa"
    )


    @Before
    fun setUp() {
        repository = DeliveryRepositoryImpl(dao)
    }

    @Test
    fun `getDeliveries should return flow of deliveries`() = runTest {
        val expectedList = listOf(mockDelivery)
        every { dao.getAll() } returns flowOf(expectedList)
        val result = repository.getAlldeliveries().getOrNull()?.first()
        assertEquals(expectedList.map { it.toModel() }, result)
    }

    @Test
    fun `saveDelivery should call dao insert`() = runTest {
        val delivery = mockDelivery.toModel()
        coEvery { dao.insert(any()) } just Runs
        repository.insertDelivery(delivery)
        coVerify { dao.insert(delivery.toEntity()) }
    }

    @Test
    fun `deleteDelivery should call dao delete`() = runTest {
        val delivery = mockDelivery.toModel()
        coEvery { dao.delete(any()) } just Runs
        repository.deleteDelivery(delivery)
        coVerify { dao.delete(delivery.toEntity()) }
    }

    @Test
    fun `updateDelivery should call dao update`() = runTest {
        val delivery = mockDelivery.toModel()
        coEvery { dao.update(any()) } just Runs
        repository.updateDelivery(delivery)
        coVerify { dao.update(delivery.toEntity()) }
    }
}
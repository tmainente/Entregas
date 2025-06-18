package com.example.entregas.domain.usecase
import com.example.entregas.domain.model.Delivery
import com.example.entregas.domain.repository.DeliveryRepository
import com.example.entregas.domain.usecases.DeleteDeliveryUseCase
import com.example.entregas.domain.usecases.SaveDeliveryUseCase
import com.example.entregas.domain.usecases.ShowDeliveryUseCase
import com.example.entregas.domain.usecases.UpdateDeliveryUseCase
import com.example.entregas.util.FakeDeliveryRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeliveryUseCaseTest {

    private lateinit var repository: DeliveryRepository
    private lateinit var insertUseCase: SaveDeliveryUseCase
    private lateinit var updateUseCase: UpdateDeliveryUseCase
    private lateinit var deleteUseCase: DeleteDeliveryUseCase
    private lateinit var getAllUseCase: ShowDeliveryUseCase

    @Before
    fun setup() {
        repository = FakeDeliveryRepository()
        insertUseCase = SaveDeliveryUseCase(repository)
        updateUseCase = UpdateDeliveryUseCase(repository)
        deleteUseCase = DeleteDeliveryUseCase(repository)
        getAllUseCase = ShowDeliveryUseCase(repository)
    }

    @Test
    fun `should add a new delivery to repository`() = runTest {
        val entrega = Delivery(
            id = 1,
            quantPackage = 5,
            dateLimit = "2025-07-01",
            nameClient = "João Silva",
            cpfClient = "12345678900",
            cep = "01001-000",
            uf = "SP",
            city = "São Paulo",
            neighborhood = "Centro",
            street = "Rua A",
            number = "123",
            complement = null
        )

        insertUseCase(entrega)

        val result = repository.getAlldeliveries().first()
        assertEquals(1, result.size)
        assertEquals(entrega, result.first())
    }

    @Test
    fun `should update an existing delivery`() = runTest {
        val delivery = Delivery(1, 1, "2025-01-01", "Cliente", "00000000000", "00000-000", "SP", "Cidade", "Bairro", "Rua", "123", null)
        insertUseCase(delivery)

        val updated = delivery.copy(nameClient = "Novo Nome")
        updateUseCase(updated)

        val result = repository.getAlldeliveries().first().first()
        assertEquals("Novo Nome", result.nameClient)
    }

    @Test
    fun `should delete an existing delivery`() = runTest {
        val delivery = Delivery(2, 1, "2025-01-01", "Cliente 2", "11111111111", "11111-111", "SP", "Cidade", "Bairro", "Rua", "321", null)
        insertUseCase(delivery)
        deleteUseCase(delivery)

        val deliveries = repository.getAlldeliveries().first()
        assertEquals(0, deliveries.size)
    }

    @Test
    fun `should get all deliveries`() = runTest {
        val e1 = Delivery(4, 1, "2025-01-01", "Cliente A", "123", "00000-000", "SP", "Cidade", "Bairro", "Rua", "1", null)
        val e2 = Delivery(5, 2, "2025-01-02", "Cliente B", "456", "11111-111", "SP", "Cidade", "Bairro", "Rua", "2", null)
        insertUseCase(e1)
        insertUseCase(e2)

        val deliveries = getAllUseCase().first()
        assertEquals(2, deliveries.size)
    }
}

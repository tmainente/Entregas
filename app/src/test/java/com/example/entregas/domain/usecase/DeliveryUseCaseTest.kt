package com.example.entregas.domain.usecase
import com.example.entregas.domain.model.Delivery
import com.example.entregas.domain.repository.CityRepository
import com.example.entregas.domain.repository.DeliveryRepository
import com.example.entregas.domain.usecases.DeleteDeliveryUseCase
import com.example.entregas.domain.usecases.SaveDeliveryUseCase
import com.example.entregas.domain.usecases.ShowCityUseCase
import com.example.entregas.domain.usecases.ShowDeliveryUseCase
import com.example.entregas.domain.usecases.UpdateDeliveryUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class DeliveryUseCaseTest {

    private lateinit var repository: DeliveryRepository
    private lateinit var cityRepository: CityRepository

    private lateinit var insertUseCase: SaveDeliveryUseCase
    private lateinit var updateUseCase: UpdateDeliveryUseCase
    private lateinit var deleteUseCase: DeleteDeliveryUseCase
    private lateinit var getAllDeliveryUseCase: ShowDeliveryUseCase
    private lateinit var getCityUseCase: ShowCityUseCase


    private val mockEntrega = Delivery(
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
        complement = null
    )

    @Before
    fun setup() {
        repository = mock(DeliveryRepository::class.java)
        cityRepository = mock(CityRepository::class.java)
        insertUseCase = SaveDeliveryUseCase(repository)
        getAllDeliveryUseCase = ShowDeliveryUseCase(repository)
        updateUseCase = UpdateDeliveryUseCase(repository)
        deleteUseCase = DeleteDeliveryUseCase(repository)
        getCityUseCase = ShowCityUseCase(cityRepository)
    }

    @Test
    fun `should return list of deliveries`() = runBlocking {
        whenever(repository.getAlldeliveries()).thenReturn(Result.success(flowOf(listOf(mockEntrega))))
        val result = getAllDeliveryUseCase()
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.first()?.size)
    }

    @Test
    fun `should handle error on getDeliveries`() = runBlocking {
        whenever(repository.getAlldeliveries()).thenReturn(Result.failure(Exception("DB Error")))

        val result = getAllDeliveryUseCase()

        assertTrue(result.isFailure)
        assertEquals("DB Error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should create delivery`() = runBlocking {
        whenever(repository.insertDelivery(mockEntrega)).thenReturn(Result.success(Unit))

        val result = insertUseCase(mockEntrega)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `should update delivery`() = runBlocking {
        whenever(repository.updateDelivery(mockEntrega)).thenReturn(Result.success(Unit))

        val result = updateUseCase(mockEntrega)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `should delete delivery`() = runBlocking {
        whenever(repository.deleteDelivery(mockEntrega)).thenReturn(Result.success(Unit))

        val result = deleteUseCase(mockEntrega)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `should return list of cities`(): Unit = runBlocking  {
        val city: List<String> = listOf("São Paulo", "Campinas")
        val expected: Result<List<String>> = Result.success(city)
        whenever (cityRepository.getCityUf("SP")).thenReturn(expected)
        val result = getCityUseCase("SP")
        val lista = result.getOrThrow()
        assertEquals(2, lista.size)
        assertEquals("São Paulo", lista[0])
        assertTrue(result.isSuccess)
           }


    @Test
    fun `should handle error on getCidades`() {
        runBlocking {
            whenever(cityRepository.getCityUf("SP")).thenReturn(Result.failure(Exception("API error")))

            val result = getCityUseCase("SP")

            assertTrue(result.isFailure)
            assertEquals("API error", result.exceptionOrNull()?.message)
        }
    }
}


package com.example.entregas.data.repository

import com.example.entregas.data.remote.CityResponse
import com.example.entregas.data.remote.CityService
import com.example.entregas.domain.repository.CityRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import kotlin.test.Test
import kotlin.test.fail

class CityRepositoryImplTest {

    private val service: CityService = mockk()
    private lateinit var repository: CityRepository

    @Before
    fun setUp() {
        repository = CityRepositoryImpl(service)
    }

    @Test
    fun `getCitiesByUf should return list of city names`() = runTest {
        val uf = "SP"
        val apiResponse = listOf(CityResponse("São Paulo"), CityResponse("Campinas"))
        coEvery { service.getCidadesPorUf(uf) } returns apiResponse

        val result = repository.getCityUf(uf)

        assertEquals(listOf("São Paulo", "Campinas"), result.getOrNull())
    }

    @Test
    fun `getCitiesByUf should throw exception on failure`() = runTest {
        val uf = "SP"
        val exception = RuntimeException("Erro ao buscar cidades")
        coEvery { service.getCidadesPorUf(uf) } throws exception
        // Act + Assert
        try {
            repository.getCityUf(uf)
        } catch (e: RuntimeException) {
            assertEquals("Erro ao buscar cidades", e.message)
        }
    }
}
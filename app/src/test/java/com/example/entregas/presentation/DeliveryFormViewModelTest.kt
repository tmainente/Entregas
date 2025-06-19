package com.example.entregas.presentation

import app.cash.turbine.test
import com.example.entregas.util.DeliveryUiState
import com.example.entregas.domain.usecases.SaveDeliveryUseCase
import com.example.entregas.domain.usecases.ShowCityUseCase
import com.example.entregas.domain.usecases.UpdateDeliveryUseCase
import com.example.entregas.util.dispatcher
import com.example.entregas.util.mockDelivery
import com.example.entregas.util.testDispatcherProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertTrue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@ExperimentalCoroutinesApi
class DeliveryFormViewModelTest {
    private lateinit var saveDeliveryUseCase: SaveDeliveryUseCase
    private lateinit var updateDeliveryUseCase: UpdateDeliveryUseCase
    private lateinit var showCityUseCase: ShowCityUseCase
    private lateinit var viewModel: DeliveryFormViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        saveDeliveryUseCase = mockk<SaveDeliveryUseCase>(relaxed = true)
        updateDeliveryUseCase =mockk<UpdateDeliveryUseCase>(relaxed = true)
        showCityUseCase = mockk<ShowCityUseCase>(relaxed = true)
        viewModel = DeliveryFormViewModel(saveDeliveryUseCase, updateDeliveryUseCase,
            showCityUseCase, testDispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Success when delivery is saved successfully`() = runTest {
        coEvery { saveDeliveryUseCase(any()) } returns Result.success(Unit)
        viewModel.saveDelivery(mockDelivery)
        advanceUntilIdle()
        viewModel.uiState.test {
            viewModel.saveDelivery(mockDelivery)
            assertTrue(awaitItem() is DeliveryUiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Error when save delivery fails`() = runTest {
        coEvery { saveDeliveryUseCase(any()) } returns Result.failure(RuntimeException("Erro salvar"))

        viewModel.saveDelivery(mockDelivery)
        advanceUntilIdle()
        viewModel.uiState.test {
            val errorState = awaitItem()
            assertTrue(errorState is DeliveryUiState.Error)
            assertEquals("Erro ao salvar: Erro salvar", (errorState as DeliveryUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Success when delivery is updated successfully`() = runTest {
        coEvery { updateDeliveryUseCase(any()) } returns Result.success(Unit)
        viewModel.updateDelivery(mockDelivery)
        advanceUntilIdle()
        viewModel.uiState.test {
            assertTrue(awaitItem() is DeliveryUiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Error when update delivery fails`() = runTest {
        coEvery { updateDeliveryUseCase(any()) } returns Result.failure(RuntimeException("Erro atualizar"))
        viewModel.updateDelivery(mockDelivery)
        advanceUntilIdle()
        viewModel.uiState.test {
            val errorState = awaitItem()
            assertTrue(errorState is DeliveryUiState.Error)
            assertEquals("Erro ao atualizar: Erro atualizar", (errorState as DeliveryUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return list of cities on success`() = runTest {
        val cities = listOf("São Paulo", "Campinas", "Santos")
        coEvery { showCityUseCase("SP") } returns Result.success(cities)
        viewModel.fetchCitiesByUf("SP")
        advanceUntilIdle()
        assertEquals(cities, viewModel.cities.value)
    }

    @Test
    fun `should not change cities list on failure`() = runTest {
        val exception = RuntimeException("Falha na API")
        coEvery { showCityUseCase("SP") } returns Result.failure(exception)
        viewModel.fetchCitiesByUf("SP")
        advanceUntilIdle()
        assertTrue(viewModel.cities.value.isEmpty())
    }

}
package com.example.entregas.presentation

import app.cash.turbine.test
import com.example.entregas.domain.model.Delivery
import com.example.entregas.util.DeliveryUiState
import com.example.entregas.domain.usecases.DeleteDeliveryUseCase
import com.example.entregas.domain.usecases.ShowDeliveryUseCase
import com.example.entregas.util.DispatcherProvider
import com.example.entregas.util.MainDispatcherRule
import com.example.entregas.util.dispatcher
import com.example.entregas.util.mockDelivery
import com.example.entregas.util.testDispatcherProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertTrue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test

@ExperimentalCoroutinesApi
class DeliveryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var showDeliveryUseCase: ShowDeliveryUseCase
    private lateinit var deleteDeliveryUseCase: DeleteDeliveryUseCase


    private lateinit var viewModel: DeliveryViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        showDeliveryUseCase = mockk<ShowDeliveryUseCase>(relaxed = true)
        deleteDeliveryUseCase =mockk<DeleteDeliveryUseCase>(relaxed = true)
        viewModel = DeliveryViewModel(deleteDeliveryUseCase, showDeliveryUseCase, testDispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Success when deliveries loaded successfully`() = runTest {
        val mockList = listOf(mockDelivery)
        val mockFlow = flowOf(mockList)
        coEvery { showDeliveryUseCase() } returns(Result.success(mockFlow))
        viewModel.loadDeliveries()
        advanceUntilIdle()

        viewModel.uiState.test {
            // Assert
            assertTrue(awaitItem() is DeliveryUiState.Success)
            cancelAndIgnoreRemainingEvents()

        }
        assertEquals(mockList, viewModel.deliveries.value)


    }

    @Test
    fun `should emit Error when getDeliveries fails`() = runTest {
        val exception = RuntimeException("Falha ao carregar entregas")
        coEvery { showDeliveryUseCase.invoke() } returns Result.failure(exception)
        viewModel.loadDeliveries()
        advanceUntilIdle()
        viewModel.uiState.test {
            // Assert
            val state = awaitItem()
            assertTrue( state is DeliveryUiState.Error)
            assertEquals("Falha ao carregar entregas", (state as DeliveryUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Success when delivery is deleted successfully`() = runTest {
        coEvery { deleteDeliveryUseCase(any()) } returns Result.success(Unit)

        viewModel.deleteDelivery(mockDelivery)
        advanceUntilIdle()
        viewModel.uiState.test {
            assertTrue(awaitItem() is DeliveryUiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Error when delete delivery fails`() = runTest {
        coEvery { deleteDeliveryUseCase(any()) } returns Result.failure(RuntimeException("Erro delete"))
        viewModel.deleteDelivery(mockDelivery)
        advanceUntilIdle()
        viewModel.uiState.test {
            val errorState = awaitItem()
            assertTrue(errorState is DeliveryUiState.Error)
            assertEquals("Erro ao excluir: Erro delete", (errorState as DeliveryUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

}
package com.example.entregas.presentation

import com.example.entregas.domain.usecases.DeleteDeliveryUseCase
import com.example.entregas.domain.usecases.ShowDeliveryUseCase
import com.example.entregas.util.MainDispatcherRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DeliveryViewModelTest {

    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var showDeliveryUseCase: ShowDeliveryUseCase
    private lateinit var deleteDeliveryUseCase: DeleteDeliveryUseCase


    private lateinit var viewModel: DeliveryViewModel

    fun setUp() {
        showDeliveryUseCase = mockk<ShowDeliveryUseCase>()
        deleteDeliveryUseCase = mockk<DeleteDeliveryUseCase>()
        viewModel = DeliveryViewModel(deleteDeliveryUseCase, showDeliveryUseCase, mainDispatcherRule.testDispatcher)
    }


}
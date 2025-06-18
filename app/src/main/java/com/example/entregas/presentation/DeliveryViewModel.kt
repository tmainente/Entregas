package com.example.entregas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entregas.domain.model.Delivery
import com.example.entregas.domain.usecases.DeleteDeliveryUseCase
import com.example.entregas.domain.usecases.ShowDeliveryUseCase
import com.example.entregas.util.DeliveryUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeliveryViewModel(
    private val deleteDeliveryUseCase: DeleteDeliveryUseCase,
    private val listDeliveriesUseCase: ShowDeliveryUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _deliveries = MutableStateFlow<List<Delivery>>(emptyList())
    val deliveries: StateFlow<List<Delivery>> = _deliveries.asStateFlow()

    private val _uiState = MutableStateFlow<DeliveryUiState>(DeliveryUiState.Init)
    val uiState: StateFlow<DeliveryUiState> = _uiState.asStateFlow()

    companion object {
        private const val SUCCESS_DELETE_MESSAGE = "Entrega excluída com sucesso"
        private const val ERROR_DELETE_MESSAGE = "Erro ao excluir: %s"
    }

    init {
        loadDeliveries()
    }

    fun loadDeliveries() {
        viewModelScope.launch(dispatcher) {
            listDeliveriesUseCase().collect { _deliveries.value = it }
        }
    }

    fun deleteDelivery(delivery: Delivery) {
        viewModelScope.launch(dispatcher) {
            _uiState.value = DeliveryUiState.Loading
            try {
                deleteDeliveryUseCase(delivery)
                _uiState.value = DeliveryUiState.Success(SUCCESS_DELETE_MESSAGE)
            } catch (e: Exception) {
                _uiState.value = DeliveryUiState.Error(String.format(ERROR_DELETE_MESSAGE, e.message))
            }
        }
    }

    fun clearUiState() {
        _uiState.value = DeliveryUiState.Init
    }
}

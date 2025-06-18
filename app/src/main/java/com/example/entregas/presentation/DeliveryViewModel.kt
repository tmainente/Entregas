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

    private val _cities = MutableStateFlow<List<String>>(emptyList())
    val cities: StateFlow<List<String>> = _cities.asStateFlow()

    private val _uiState = MutableStateFlow<DeliveryUiState>(DeliveryUiState.Init)
    val uiState: StateFlow<DeliveryUiState> = _uiState.asStateFlow()

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
                _uiState.value = DeliveryUiState.Success("Entrega excluída com sucesso")
            } catch (e: Exception) {
                _uiState.value = DeliveryUiState.Error("Erro ao excluir: ${e.message}")
            }
        }
    }

    fun clearUiState() {
        _uiState.value = DeliveryUiState.Init
    }
}

package com.example.entregas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entregas.domain.model.Delivery
import com.example.entregas.domain.usecases.DeleteDeliveryUseCase
import com.example.entregas.domain.usecases.ShowDeliveryUseCase
import com.example.entregas.util.DefaultDispatcherProvider
import com.example.entregas.util.DeliveryUiState
import com.example.entregas.util.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeliveryViewModel(
    private val deleteDeliveryUseCase: DeleteDeliveryUseCase,
    private val listDeliveriesUseCase: ShowDeliveryUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : ViewModel() {

    private val _deliveries = MutableStateFlow<List<Delivery>>(emptyList())
    val deliveries: StateFlow<List<Delivery>> = _deliveries.asStateFlow()

    private val _uiState = MutableStateFlow<DeliveryUiState>(DeliveryUiState.Init)
    val uiState: StateFlow<DeliveryUiState> = _uiState.asStateFlow()

    companion object {
        private const val SUCCESS_DELETE_MESSAGE = "Entrega excluída com sucesso"
        private const val ERROR_DELETE_MESSAGE = "Erro ao excluir: %s"
    }


    fun loadDeliveries() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = DeliveryUiState.Loading

            listDeliveriesUseCase().onSuccess { flowDelivery ->
                flowDelivery.collect { deliveries ->
                    _deliveries.value = deliveries
                    if(deliveries.isEmpty()) {
                        _uiState.value = DeliveryUiState.Error("vazio")
                } else{
                    _uiState.value = DeliveryUiState.Success("Entregas carregadas com sucesso")
                }

                    }

            }.onFailure {
                _uiState.value = DeliveryUiState.Error(it.message ?: "Erro desconhecido")
            }
            }
    }

    fun deleteDelivery(delivery: Delivery) {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = DeliveryUiState.Loading
                deleteDeliveryUseCase(delivery).onSuccess {
                    _uiState.value = DeliveryUiState.Success(SUCCESS_DELETE_MESSAGE)
                } .onFailure {
                    _uiState.value = DeliveryUiState.Error(String.format(ERROR_DELETE_MESSAGE, it.message))
                }

        }
    }

    fun clearUiState() {
        _uiState.value = DeliveryUiState.Init
    }
}

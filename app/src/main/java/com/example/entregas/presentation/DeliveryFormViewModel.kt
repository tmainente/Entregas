package com.example.entregas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entregas.domain.model.Delivery
import com.example.entregas.domain.usecases.DeleteDeliveryUseCase
import com.example.entregas.domain.usecases.SaveDeliveryUseCase
import com.example.entregas.domain.usecases.ShowCityUseCase
import com.example.entregas.domain.usecases.ShowDeliveryByIdUseCase
import com.example.entregas.domain.usecases.ShowDeliveryUseCase
import com.example.entregas.domain.usecases.UpdateDeliveryUseCase
import com.example.entregas.util.DeliveryUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeliveryFormViewModel(
    private val saveDeliveryUseCase: SaveDeliveryUseCase,
    private val updateDeliveryUseCase: UpdateDeliveryUseCase,
    private val deliveryByIdUseCase: ShowDeliveryByIdUseCase,
    private val fetchCitiesByUfUseCase: ShowCityUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {


    private val _cities = MutableStateFlow<List<String>>(emptyList())
    val cities: StateFlow<List<String>> = _cities.asStateFlow()

    private val _uiState = MutableStateFlow<DeliveryUiState>(DeliveryUiState.Init)
    val uiState: StateFlow<DeliveryUiState> = _uiState.asStateFlow()

    init {
    }

    fun saveDelivery(delivery: Delivery) {
        viewModelScope.launch(dispatcher) {
            _uiState.value = DeliveryUiState.Loading
            try {
                saveDeliveryUseCase(delivery)
                _uiState.value = DeliveryUiState.Success("Entrega cadastrada com sucesso")
            } catch (e: Exception) {
                _uiState.value = DeliveryUiState.Error("Erro ao salvar: ${e.message}")
            }
        }
    }

    fun updateDelivery(delivery: Delivery) {
        viewModelScope.launch(dispatcher) {
            _uiState.value = DeliveryUiState.Loading
            try {
                updateDeliveryUseCase(delivery)
                _uiState.value = DeliveryUiState.Success("Entrega atualizada com sucesso")
            } catch (e: Exception) {
                _uiState.value = DeliveryUiState.Error("Erro ao atualizar: ${e.message}")
            }
        }
    }

    fun fetchCitiesByUf(uf: String) {
        viewModelScope.launch(dispatcher) {
            _uiState.value = DeliveryUiState.Loading
            try {
                _cities.value = fetchCitiesByUfUseCase(uf)
                _uiState.value = DeliveryUiState.Init
            } catch (e: Exception) {
                _uiState.value = DeliveryUiState.Error("Erro ao buscar cidades: ${e.message}")
            }
        }
    }

    fun getDeliveryById(id: Long): Delivery? {
        var entrega: Delivery? = null
        viewModelScope.launch(dispatcher) {
            entrega = deliveryByIdUseCase(id)
        }
        return entrega
    }

    fun clearUiState() {
        _uiState.value = DeliveryUiState.Init
    }
}

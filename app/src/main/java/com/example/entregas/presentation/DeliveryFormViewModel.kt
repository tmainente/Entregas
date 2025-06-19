package com.example.entregas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entregas.domain.model.Delivery
import com.example.entregas.domain.usecases.SaveDeliveryUseCase
import com.example.entregas.domain.usecases.ShowCityUseCase
import com.example.entregas.domain.usecases.UpdateDeliveryUseCase
import com.example.entregas.util.DefaultDispatcherProvider
import com.example.entregas.util.DeliveryUiState
import com.example.entregas.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeliveryFormViewModel(
    private val saveDeliveryUseCase: SaveDeliveryUseCase,
    private val updateDeliveryUseCase: UpdateDeliveryUseCase,
    private val fetchCitiesByUfUseCase: ShowCityUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : ViewModel() {


    private val _cities = MutableStateFlow<List<String>>(emptyList())
    val cities: StateFlow<List<String>> = _cities.asStateFlow()

    private val _uiState = MutableStateFlow<DeliveryUiState>(DeliveryUiState.Init)
    val uiState: StateFlow<DeliveryUiState> = _uiState.asStateFlow()

    fun saveDelivery(delivery: Delivery) {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = DeliveryUiState.Loading
                saveDeliveryUseCase(delivery).onSuccess {
                    _uiState.value = DeliveryUiState.Success(DELIVERY_SUCCESSFULLY_REGISTERED)
                }.onFailure { e ->
                    _uiState.value = DeliveryUiState.Error(String.format(ERROR_SAVING_DELIVERY, e.message))

                }
        }
    }

    fun updateDelivery(delivery: Delivery) {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = DeliveryUiState.Loading
                updateDeliveryUseCase(delivery).onSuccess {
                    _uiState.value = DeliveryUiState.Success(DELIVERY_SUCCESSFULLY_UPDATED)
                }.onFailure { e ->
                    _uiState.value = DeliveryUiState.Error(String.format(ERROR_UPDATING_DELIVERY, e.message))
                }
        }
    }

    fun fetchCitiesByUf(uf: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = DeliveryUiState.Loading
            fetchCitiesByUfUseCase(uf).onSuccess { cities ->
                _cities.value = cities
                _uiState.value = DeliveryUiState.Init
            }.onFailure { e ->
                _uiState.value = DeliveryUiState.Error(String.format(ERROR_FETCHING_CITIES, e.message))
            }
        }
    }

    companion object {
        const val ERROR_SAVING_DELIVERY = "Erro ao salvar: %s"
        const val ERROR_FETCHING_CITIES = "Erro ao buscar cidades: %s"
        const val ERROR_UPDATING_DELIVERY = "Erro ao atualizar: %s"
        const val DELIVERY_SUCCESSFULLY_REGISTERED = "Entrega cadastrada com sucesso"
        const val DELIVERY_SUCCESSFULLY_UPDATED = "Entrega atualizada com sucesso"
    }
}

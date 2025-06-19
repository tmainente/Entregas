package com.example.entregas.util

sealed class DeliveryUiState {
    data object Init : DeliveryUiState()
    data object Loading : DeliveryUiState()
    data class Success(val message: String) : DeliveryUiState()
    data class Error(val message: String) : DeliveryUiState()
}
package com.garcia.modulocredito.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garcia.modulocredito.domain.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreditViewModel() : ViewModel() {

    private val _state = MutableStateFlow(UiState(loading = true))
    val state: StateFlow<UiState> = _state

    init {
        viewModelScope.launch {
            try {
                _state.value = UiState(
                    amount = 2000000.toBigDecimal(),
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, error = e.message ?: "Error")
            }
        }
    }

    fun monthlyInstallment(): Double = 0.0

    fun totalToReturn(): Double = 0.0

    fun finalDueDate(): String = "10/10/25"
}
package com.garcia.modulocredito.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garcia.modulocredito.domain.CreditRepository
import com.garcia.modulocredito.domain.model.UiState
import com.garcia.modulocredito.domain.usecase.CalculateInstallmentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class CreditViewModel(
    private val repo: CreditRepository,
    private val calculateInstallmentUseCase: CalculateInstallmentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState(loading = true))
    val state: StateFlow<UiState> = _state

    init {
        viewModelScope.launch {
            try {
                val cfg = repo.fetchConfig()
                _state.value = UiState(
                    config = cfg,
                    loading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, error = e.message ?: "Error")
            }
        }
    }

    fun onAmountChanged(newAmount: Float) {
        val amount = (newAmount.toLong() / 100_000L) * 100_000L
        _state.value = _state.value.copy(amount = amount.toBigDecimal())
    }

    fun onTermChanged(newTerm: Float) {
        _state.value = _state.value.copy(term = newTerm.toInt())
    }

    fun monthlyInstallment(): Double {
        if (_state.value.term <= 0) return 0.0
        val amount = _state.value.amount
        val term = _state.value.term
        val rate = _state.value.rate
        return calculateInstallmentUseCase(amount.toDouble(), rate, term)
    }

    fun totalToReturn(): Double {
        val cuota = monthlyInstallment()
        return cuota * _state.value.term
    }

    fun finalDueDate(): LocalDate {
        return LocalDate.now().plusMonths(_state.value.term.toLong())
    }
}

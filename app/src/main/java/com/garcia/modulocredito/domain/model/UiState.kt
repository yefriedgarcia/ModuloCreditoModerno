package com.garcia.modulocredito.domain.model

import java.math.BigDecimal

data class UiState(
    val config: CreditConfig? = null,
    val amount: BigDecimal = BigDecimal(1500000),
    val term: Int = 1,
    val rate: Double = 18.0,
    val loading: Boolean = false,
    val error: String? = null
)

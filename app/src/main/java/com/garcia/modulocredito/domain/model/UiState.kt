package com.garcia.modulocredito.domain.model

import java.math.BigDecimal

data class UiState(
    val amount: BigDecimal = BigDecimal(2000000),
    val term: Int=4,
    val rate: Double=18.0,
    val loading: Boolean=false,
    val error: String?=null
)

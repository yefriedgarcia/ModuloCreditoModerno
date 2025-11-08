package com.garcia.modulocredito.domain.model

import java.math.BigDecimal

data class CreditConfig(
    val minAmount: BigDecimal = BigDecimal(100000),
    val maxAmount: BigDecimal = BigDecimal(3000000),
    val maxTermMonths: Int = 12,
    val minTermMonths: Int = 1
)

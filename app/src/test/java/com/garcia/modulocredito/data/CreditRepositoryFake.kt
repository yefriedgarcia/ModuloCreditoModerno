package com.garcia.modulocredito.data

import com.garcia.modulocredito.domain.CreditRepository
import com.garcia.modulocredito.domain.model.CreditConfig

class CreditRepositoryFake : CreditRepository {
    override suspend fun fetchConfig(): CreditConfig = CreditConfig(
        minAmount = 10000.0.toBigDecimal(),
        maxAmount = 100000.0.toBigDecimal(),
        maxTermMonths = 12,
        minTermMonths = 1
    )
}

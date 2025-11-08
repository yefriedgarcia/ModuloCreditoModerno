package com.garcia.modulocredito.data.repository

import com.garcia.modulocredito.domain.CreditRepository
import com.garcia.modulocredito.domain.model.CreditConfig

class CreditRepositoryImpl() : CreditRepository {
    override suspend fun fetchConfig() = CreditConfig()
}

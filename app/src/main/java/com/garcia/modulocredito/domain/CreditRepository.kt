package com.garcia.modulocredito.domain

import com.garcia.modulocredito.domain.model.CreditConfig

interface CreditRepository {
    suspend fun fetchConfig(): CreditConfig
}

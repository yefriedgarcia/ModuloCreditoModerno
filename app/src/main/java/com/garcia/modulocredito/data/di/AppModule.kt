package com.garcia.modulocredito.data.di

import com.garcia.modulocredito.data.repository.CreditRepositoryImpl
import com.garcia.modulocredito.domain.CreditRepository
import com.garcia.modulocredito.domain.usecase.CalculateInstallmentUseCase
import com.garcia.modulocredito.ui.viewmodel.CreditViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CreditRepository> { CreditRepositoryImpl() }
    single { CalculateInstallmentUseCase() }
    viewModel { CreditViewModel(get(), get()) }
}

package com.garcia.modulocredito.data.di

import com.garcia.modulocredito.ui.viewmodel.CreditViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { CreditViewModel() }
}

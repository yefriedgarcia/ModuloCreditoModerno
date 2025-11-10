package com.garcia.modulocredito.ui.viewmodel

import app.cash.turbine.test
import com.garcia.modulocredito.data.CreditRepositoryFake
import com.garcia.modulocredito.domain.usecase.CalculateInstallmentUseCase
import com.garcia.modulocredito.utils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import java.math.BigDecimal
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreditViewModelTest {

    @get:Rule
    val testDispatchers = MainDispatcherRule()
    private lateinit var creditViewModel: CreditViewModel

    private lateinit var fakeRepository: CreditRepositoryFake

    @MockK
    private lateinit var useCase: CalculateInstallmentUseCase

    @Before
    fun setUp() {

        useCase = mockk()
        fakeRepository = CreditRepositoryFake()

        creditViewModel = CreditViewModel(fakeRepository, useCase)
    }

    @Test
    fun givenLoadedState_WhenInitialized_ThenStateUpdatedCorrectly() = runTest {
        creditViewModel.state.test {
            val emission = awaitItem()
            assertThat(emission.loading).isTrue()
            assertThat(emission.config).isNull()


            creditViewModel.initialize()
            val emission2 = awaitItem()
            assertThat(emission2.loading).isFalse()
            assertThat(emission2.config).isEqualTo(fakeRepository.fetchConfig())

        }
    }

    @Test
    fun givenAmount_WhenOnAmountChanged_ThenUpdatedNewAmount() = runTest {
        // given
        val newAmount = 200_000F

        //when
        creditViewModel.onAmountChanged(newAmount)

        // THEN
        assertThat(creditViewModel.state.value.amount).isEqualTo(BigDecimal(200000))
    }

    @Test
    fun givenAmount_WhenOnAmountChanged_ThenRoundToNearestDown100k() = runTest {
        // given
        val newAmount = 295_000F

        //when
        creditViewModel.onAmountChanged(newAmount)

        // THEN
        assertThat(creditViewModel.state.value.amount).isEqualTo(BigDecimal(200000))
    }

    @Test
    fun givenAmountBelowMinValue_WhenOnAmountChanged_ThenReturnZero() = runTest {
        // given
        val newAmount = 99_999F

        //when
        creditViewModel.onAmountChanged(newAmount)

        // THEN
        assertThat(creditViewModel.state.value.amount).isEqualTo(BigDecimal(0))
    }

    @Test
    fun givenNewTerm_WhenOnTermChanged_ThenUpdatedState() = runTest {
        // given
        val newTerm = 6F

        //when
        creditViewModel.onTermChanged(newTerm)

        // then
        assertThat(creditViewModel.state.value.term).isEqualTo(newTerm.toInt())
    }

    @Test
    fun givenTermZero_WhenMonthlyInstallment_ThenReturnZero() = runTest {
        // given
        val newTerm = 0f

        //when
        creditViewModel.onTermChanged(newTerm)
        creditViewModel.monthlyInstallment()

        // then
        assertThat(creditViewModel.monthlyInstallment()).isEqualTo(0.0)
    }

    @Test
    fun givenDefaultValues_WhenMonthlyInstallment_ThenReturnAmountCorrectly() = runTest {
        // given
        every { useCase.invoke(any(), any(), any()) } returns 100000.0

        // when
        creditViewModel.monthlyInstallment()

        // then
        assertThat(creditViewModel.monthlyInstallment()).isGreaterThan(0)
    }
}

package com.garcia.modulocredito.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculateInstallmentUseCaseTest {

    private lateinit var useCaseTest: CalculateInstallmentUseCase

    @Before
    fun setUp() {
        useCaseTest = CalculateInstallmentUseCase()
    }

    @Test
    fun givenInvalidMonths_thenReturnZero() {
        // given
        val amount = 1000.0
        val annualRatePercent = 10.0
        val months = 0

        //when
        val result = useCaseTest.invoke(amount, annualRatePercent, months)

        // then
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun givenZeroAnnualRate_thenReturnAmountDividedByMonths() {
        // given
        val amount = 1000.0
        val annualRatePercent = 0.0
        val months = 2
        // when
        val result = useCaseTest.invoke(amount, annualRatePercent, months)

        // then
        assertEquals(500.0, result, 0.0001)
    }

    @Test
    fun givenZeroAmount_thenReturnZero() {
        // GIVEN
        val amount = 0.0
        val annualRatePercent = 10.0
        val months = 2

        // WHEN
        val result = useCaseTest.invoke(amount, annualRatePercent, months)

        // THEN
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun givenValidInputs_thenCalculateInstallmentCorrectly() {
        // GIVEN
        val amount = 100000.0
        val annualRatePercent = 18.0
        val months = 2

        // WHEN
        val result = useCaseTest.invoke(amount, annualRatePercent, months)

        // THEN
        assertEquals(51044.03, result, 0.01)
    }
}

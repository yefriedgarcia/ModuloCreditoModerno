package com.garcia.modulocredito.domain.usecase

import kotlin.math.pow

/**
 * @param amount Monto del crédito (capital) en unidades monetarias.
 * @param annualRatePercent Tasa efectiva anual (EA) en porcentaje.
 * @param months Plazo en meses (número de pagos).
 *
 * @return Valor de la cuota mensual fija. Retorna 0.0 si el plazo es inválido.
 */
class CalculateInstallmentUseCase {
    operator fun invoke(amount: Double, annualRatePercent: Double, months: Int): Double {
        val installmentYear = (annualRatePercent / 100.0)
        val installmentMonth = (1.0 + installmentYear).pow(1.0 / 12.0) - 1.0
        if (months <= 0) return 0.0
        if (installmentMonth == 0.0) return amount / months
        return (amount * installmentMonth) / (1 - (1 + installmentMonth).pow(-months))
    }
}

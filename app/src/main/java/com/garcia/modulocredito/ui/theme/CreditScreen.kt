package com.garcia.modulocredito.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.garcia.modulocredito.ui.viewmodel.CreditViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScreen(viewModel: CreditViewModel) {
    val state by viewModel.state.collectAsState()
    val fmtCOP = remember { NumberFormat.getCurrencyInstance(Locale("es", "CO")) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Simulador de Crédito") }) }
    ) { padding ->
        if (state.loading) {
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        if (state.error != null) {
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text(state.error!!, color = MaterialTheme.colorScheme.error) }
            return@Scaffold
        }

        val min = 100000
        val max = 3000000

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Cuanto necesitas?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    Text(
                        text = "${fmtCOP.format(state.amount)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Slider(
                        value = state.amount.toFloat(),
                        onValueChange = { },
                        valueRange = min.toFloat()..max.toFloat(),
                        steps = (((max - min) / 10_000f).roundToInt()).coerceAtLeast(1) - 1,
                        colors = SliderDefaults.colors(
                            thumbColor = Purple40,
                            activeTrackColor = Purple40,
                            inactiveTrackColor = Purple80.copy(alpha = 0.5F),
                            activeTickColor = Purple40,
                            inactiveTickColor = Purple80.copy(alpha = 0.5F)
                        )
                    )

                    Text("${fmtCOP.format(min)}  →  ${fmtCOP.format(max)}",
                        style = MaterialTheme.typography.bodySmall)
                    Text("Plazo (meses): ${state.term}", style = MaterialTheme.typography.labelLarge)

                }
            }

            val cuota = viewModel.monthlyInstallment()
            val total = viewModel.totalToReturn()
            val fecha = viewModel.finalDueDate()

            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Información del prestamo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    ListItem(
                        headlineContent = { Text("¿Cuánto me cuesta (cuota mensual)?") },
                        supportingContent = { Text(fmtCOP.format(cuota)) }
                    )
                    ListItem(
                        headlineContent = { Text("¿Cuánto tengo que devolver (total)?") },
                        supportingContent = { Text(fmtCOP.format(total)) }
                    )
                    ListItem(
                        headlineContent = { Text("Fecha de cuando finaliza (último pago)") },
                        supportingContent = { Text(fecha) }
                    )
                }
            }
        }
    }
}

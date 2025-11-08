package com.garcia.modulocredito.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.garcia.modulocredito.ui.theme.Purple40
import com.garcia.modulocredito.ui.theme.Purple80
import com.garcia.modulocredito.ui.viewmodel.CreditViewModel
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScreen(viewModel: CreditViewModel) {
    val state by viewModel.state.collectAsState()
    val cfg = state.config
    val fmtCOP = remember { NumberFormat.getCurrencyInstance(Locale("es", "CO")) }
    val dateFmt = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

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

        if (cfg == null) return@Scaffold

        val min = cfg.minAmount.toFloat()
        val max = cfg.maxAmount.toFloat()

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "¿Cuanto necesitas?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Monto: ${fmtCOP.format(state.amount)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                    Slider(
                        value = state.amount.toFloat().coerceIn(min, max),
                        onValueChange = { viewModel.onAmountChanged(it) },
                        valueRange = min..max,
                        steps = (((max - min) / 100_000f).roundToInt()).coerceAtLeast(1) - 1,
                        colors = SliderDefaults.colors(
                            thumbColor = Purple40,
                            activeTrackColor = Purple40,
                            inactiveTrackColor = Purple80.copy(alpha = 0.5F),
                            activeTickColor = Purple40,
                            inactiveTickColor = Purple80.copy(alpha = 0.5F)
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = fmtCOP.format(cfg.minAmount),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = fmtCOP.format(cfg.maxAmount),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Plazo (meses): ${state.term}",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Slider(
                        value = state.term.toFloat(),
                        onValueChange = { viewModel.onTermChanged(it) },
                        valueRange = cfg.minTermMonths.toFloat()..cfg.maxTermMonths.toFloat(),
                        steps = (cfg.maxTermMonths - cfg.minTermMonths).coerceAtLeast(1) - 1,
                        colors = SliderDefaults.colors(
                            thumbColor = Purple40,
                            activeTrackColor = Purple40,
                            inactiveTrackColor = Purple80.copy(alpha = 0.5F),
                            activeTickColor = Purple40,
                            inactiveTickColor = Purple80.copy(alpha = 0.5F)
                        )
                    )
                }
            }
            val cuota = viewModel.monthlyInstallment()
            val total = viewModel.totalToReturn()
            val fecha = viewModel.finalDueDate()

            Card {
                Column(Modifier.padding(4.dp)) {
                    Text(
                        "Información del prestamo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    ListItem(
                        headlineContent = { Text("¿Cuánto me cuesta (cuota mensual)?") },
                        supportingContent = { Text(fmtCOP.format(cuota)) },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                    ListItem(
                        headlineContent = { Text("¿Cuánto tengo que devolver (total)?") },
                        supportingContent = { Text(fmtCOP.format(total)) },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent // ← hereda el color de la Card
                        )
                    )
                    ListItem(
                        headlineContent = { Text("Fecha de cuando devolver (último pago)") },
                        supportingContent = { Text(dateFmt.format(fecha)) },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent // ← hereda el color de la Card
                        )
                    )
                }
            }
        }
    }
}

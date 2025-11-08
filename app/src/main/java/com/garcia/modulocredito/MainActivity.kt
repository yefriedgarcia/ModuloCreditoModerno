package com.garcia.modulocredito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.garcia.modulocredito.ui.CreditScreen
import com.garcia.modulocredito.ui.theme.ModuloCreditoTheme
import com.garcia.modulocredito.ui.viewmodel.CreditViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ModuloCreditoTheme {
                val vm = koinViewModel<CreditViewModel>()
                CreditScreen(vm)
            }
        }
    }
}

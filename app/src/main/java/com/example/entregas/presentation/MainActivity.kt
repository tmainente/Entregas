package com.example.entregas.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.entregas.presentation.navigation.DeliveryNavGraph
import com.example.entregas.presentation.theme.EntregasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EntregasTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DeliveryNavGraph()
                }
            }
        }
    }
}


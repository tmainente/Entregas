package com.example.entregas.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.entregas.domain.model.Delivery
import com.example.entregas.presentation.DeliveryViewModel
import com.example.entregas.util.DeliveryUiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(
    viewModel: DeliveryViewModel = koinViewModel(),
    onNavigateToForm: (Long?) -> Unit
) {
    val deliveries by viewModel.deliveries.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is DeliveryUiState.Success -> snackbarHostState.showSnackbar(state.message)
            is DeliveryUiState.Error -> snackbarHostState.showSnackbar(state.message)
            else -> {}
        }
        viewModel.clearUiState()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Deliverys") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToForm(null) }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                if (uiState is DeliveryUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(deliveries) { delivery ->
                        DeliveryItem(
                            delivery = delivery,
                            onDelete = { viewModel.deleteDelivery(delivery) },
                            onEdit = { onNavigateToForm(delivery.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryItem(delivery: Delivery, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Cliente: ${delivery.nameClient}")
            Text("Cidade: ${delivery.city}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onDelete) { Text("Excluir") }
                Button(onClick = onEdit) { Text("Editar") }
            }
        }
    }
}
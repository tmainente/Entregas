package com.example.entregas.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.entregas.R
import com.example.entregas.domain.model.Delivery
import com.example.entregas.presentation.DeliveryViewModel
import com.example.entregas.util.DeliveryUiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(
    viewModel: DeliveryViewModel = koinViewModel(),
    onNavigateToForm: (Delivery?) -> Unit
) {
    val deliveries by viewModel.deliveries.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val title = stringResource(R.string.title_delivery)
    val fabLabel = stringResource(R.string.label_btn_floating)

    LaunchedEffect(Unit) {
        viewModel.loadDeliveries()
    }
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
            TopAppBar(title = { Text(title) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToForm(null) }) {
                Text(fabLabel)
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
                    items(items = deliveries, key = { it.id }) { delivery ->
                        DeliveryItem(
                            delivery = delivery,
                            onDelete = { viewModel.deleteDelivery(delivery) },
                            onEdit = { onNavigateToForm(delivery) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryItem(delivery: Delivery, onDelete: () -> Unit, onEdit: () -> Unit) {
    val clientLabel = stringResource(R.string.label_client)
    val cityLabel = stringResource(R.string.label_city)
    val deleteLabel = stringResource(R.string.label_btn_delete)
    val editLabel = stringResource(R.string.label_btn_edit)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("$clientLabel ${delivery.nameClient}")
            Text("$cityLabel ${delivery.city}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onDelete) { Text(deleteLabel) }
                Button(onClick = onEdit) { Text(editLabel) }
            }
        }
    }
}
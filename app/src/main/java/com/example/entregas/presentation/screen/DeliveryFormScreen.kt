package com.example.entregas.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.entregas.R
import com.example.entregas.domain.model.Delivery
import com.example.entregas.presentation.DeliveryFormViewModel
import org.koin.androidx.compose.koinViewModel
import kotlinx.serialization.json.Json
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryFormScreen(
    deliveryJson: String?,
    onBack: () -> Unit,
    viewModel: DeliveryFormViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val delivery = remember (deliveryJson) {
        deliveryJson?.takeIf { it.isNotEmpty() }?.let {
            Json.decodeFromString<Delivery>(URLDecoder.decode(it, "UTF-8"))
        }
    }
    val cities by viewModel.cities.collectAsState()
    val estados =  context.resources.getStringArray(R.array.UF)
    var nome by remember { mutableStateOf(delivery?.nameClient ?: "") }
    var cpf by remember { mutableStateOf(delivery?.cpfClient ?: "") }
    var cidade by remember { mutableStateOf(delivery?.city ?: "") }
    var uf by remember { mutableStateOf(delivery?.uf ?: "") }
    var rua by remember { mutableStateOf(delivery?.street ?: "") }
    var numero by remember { mutableStateOf(delivery?.number ?: "") }
    var complemento by remember { mutableStateOf(delivery?.complement ?: "") }
    var cep by remember { mutableStateOf(delivery?.cep ?: "") }
    var bairro by remember { mutableStateOf(delivery?.neighborhood ?: "") }
    var data by remember { mutableStateOf(delivery?.dateLimit ?: "") }
    var pacotes by remember { mutableStateOf(delivery?.quantPackage?.toString() ?: "1") }
    var cidadeExpanded by remember { mutableStateOf(false) }
    var ufExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(uf) {
        if (uf.isNotEmpty()) {
            viewModel.fetchCitiesByUf(uf)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (delivery != null) context.resources.getString(R.string.title_update) else
                context.resources.getString(R.string.title_new)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text(context.resources.getString(R.string.label_name)) })
            OutlinedTextField(value = cpf, onValueChange = { cpf = it }, label = { Text(context.resources.getString(R.string.label_cpf)) })
            OutlinedTextField(value = data, onValueChange = { data = it }, label = { Text(context.resources.getString(R.string.label_data)) })
            OutlinedTextField(value = cep, onValueChange = { cep = it }, label = { Text(context.resources.getString(R.string.label_cep)) })

            ExposedDropdownMenuBox(
                expanded = ufExpanded,
                onExpandedChange = { ufExpanded = !ufExpanded }
            ) {
                OutlinedTextField(
                    value = uf,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(context.resources.getString(R.string.label_uf)) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = ufExpanded,
                    onDismissRequest = { ufExpanded = false }
                ) {
                    estados.forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado) },
                            onClick = {
                                uf = estado
                                viewModel.fetchCitiesByUf(estado)
                                ufExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = cidadeExpanded,
                onExpandedChange = { cidadeExpanded = !cidadeExpanded }
            ) {
                OutlinedTextField(
                    value = cidade,
                    onValueChange = { cidade = it },
                    readOnly = true,
                    label = { Text(context.resources.getString(R.string.label_city)) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = cidadeExpanded,
                    onDismissRequest = { cidadeExpanded = false }
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                cidade = city
                                cidadeExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(value = bairro, onValueChange = { bairro = it },
                label = { Text(context.resources.getString(R.string.label_neighborhood)) })
            OutlinedTextField(value = rua, onValueChange = { rua = it },
                label = { Text(context.resources.getString(R.string.label_street)) })
            OutlinedTextField(value = numero, onValueChange = { numero = it },
                label = { Text(context.resources.getString(R.string.label_number)) })
            OutlinedTextField(value = complemento, onValueChange = { complemento = it },
                label = { Text(context.resources.getString(R.string.label_complement)) })
            OutlinedTextField(value = pacotes, onValueChange = { pacotes = it },
                label = { Text(context.resources.getString(R.string.label_package)) })

            Button(onClick = {
                val entrega = Delivery(
                    id = delivery?.id ?: 0L,
                    nameClient = nome,
                    cpfClient = cpf,
                    dateLimit = data,
                    cep = cep,
                    uf = uf,
                    city = cidade,
                    neighborhood = bairro,
                    street = rua,
                    number = numero,
                    complement = complemento.ifBlank { null },
                    quantPackage = pacotes.toIntOrNull() ?: 1
                )
                if (delivery != null) viewModel.updateDelivery(entrega)
                else viewModel.saveDelivery(entrega)
                onBack()
            }) {
                Text(context.resources.getString(R.string.label_btn_save))
            }
        }
    }
}
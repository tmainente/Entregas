package com.example.entregas.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.entregas.domain.model.Delivery
import com.example.entregas.presentation.DeliveryFormViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryFormScreen(
    deliveryId: Long?,
    onBack: () -> Unit,
    viewModel: DeliveryFormViewModel = koinViewModel()
) {
    val cities by viewModel.cities.collectAsState()
    val estados = listOf("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO")

    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var uf by remember { mutableStateOf("") }
    var rua by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var complemento by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var pacotes by remember { mutableStateOf("1") }
    var cidadeExpanded by remember { mutableStateOf(false) }
    var ufExpanded by remember { mutableStateOf(false) }

    // Carrega entrega existente, se deliveryId não for nulo
    LaunchedEffect(deliveryId) {
        deliveryId?.let {
            viewModel.getDeliveryById(it)?.let { delivery ->
                nome = delivery.nameClient
                cpf = delivery.cpfClient
                data = delivery.dateLimit
                cep = delivery.cep
                uf = delivery.uf
                cidade = delivery.city
                bairro = delivery.neighborhood
                rua = delivery.street
                numero = delivery.number
                complemento = delivery.complement ?: ""
                pacotes = delivery.quantPackage.toString()
                viewModel.fetchCitiesByUf(delivery.uf)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (deliveryId != null) "Editar Entrega" else "Nova Entrega") })
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
            OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do Cliente") })
            OutlinedTextField(value = cpf, onValueChange = { cpf = it }, label = { Text("CPF") })
            OutlinedTextField(value = data, onValueChange = { data = it }, label = { Text("Data Limite") })
            OutlinedTextField(value = cep, onValueChange = { cep = it }, label = { Text("CEP") })

            ExposedDropdownMenuBox(
                expanded = ufExpanded,
                onExpandedChange = { ufExpanded = !ufExpanded }
            ) {
                OutlinedTextField(
                    value = uf,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("UF") },
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
                    label = { Text("Cidade") },
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

            OutlinedTextField(value = bairro, onValueChange = { bairro = it }, label = { Text("Bairro") })
            OutlinedTextField(value = rua, onValueChange = { rua = it }, label = { Text("Rua") })
            OutlinedTextField(value = numero, onValueChange = { numero = it }, label = { Text("Número") })
            OutlinedTextField(value = complemento, onValueChange = { complemento = it }, label = { Text("Complemento") })
            OutlinedTextField(value = pacotes, onValueChange = { pacotes = it }, label = { Text("Qtd Pacotes") })

            Button(onClick = {
                val entrega = Delivery(
                    id = deliveryId ?: 0L,
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
                if (deliveryId != null) viewModel.updateDelivery(entrega)
                else viewModel.saveDelivery(entrega)
                onBack()
            }) {
                Text("Salvar")
            }
        }
    }
}
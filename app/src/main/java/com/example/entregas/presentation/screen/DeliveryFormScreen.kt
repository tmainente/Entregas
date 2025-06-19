package com.example.entregas.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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
    val delivery = remember(deliveryJson) {
        deliveryJson?.takeIf { it.isNotEmpty() }?.let {
            Json.decodeFromString<Delivery>(URLDecoder.decode(it, "UTF-8"))
        }
    }

    val cities by viewModel.cities.collectAsState()
    val estados = stringArrayResource(R.array.UF)

    val scrollState = rememberScrollState()
    val isEditing = delivery != null

    var nome by rememberSaveable { mutableStateOf(delivery?.nameClient.orEmpty()) }
    var cpf by rememberSaveable { mutableStateOf(delivery?.cpfClient.orEmpty()) }
    var cidade by rememberSaveable { mutableStateOf(delivery?.city.orEmpty()) }
    var uf by rememberSaveable { mutableStateOf(delivery?.uf.orEmpty()) }
    var rua by rememberSaveable { mutableStateOf(delivery?.street.orEmpty()) }
    var numero by rememberSaveable { mutableStateOf(delivery?.number.orEmpty()) }
    var complemento by rememberSaveable { mutableStateOf(delivery?.complement.orEmpty()) }
    var cep by rememberSaveable { mutableStateOf(delivery?.cep.orEmpty()) }
    var bairro by rememberSaveable { mutableStateOf(delivery?.neighborhood.orEmpty()) }
    var data by rememberSaveable { mutableStateOf(delivery?.dateLimit.orEmpty()) }
    var pacotes by rememberSaveable { mutableStateOf(delivery?.quantPackage?.toString().orEmpty()) }

    var cidadeExpanded by remember { mutableStateOf(false) }
    var ufExpanded by remember { mutableStateOf(false) }

    // Chama cidades ao mudar UF
    LaunchedEffect(uf) {
        if (uf.isNotBlank()) {
            viewModel.fetchCitiesByUf(uf)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(if (isEditing) R.string.title_update else R.string.title_new))
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DeliveryFormFields(
                nome, { nome = it },
                cpf, { cpf = it },
                data, { data = it },
                cep, { cep = it },
                uf, { selected ->
                    uf = selected
                    viewModel.fetchCitiesByUf(selected)
                }, estados, ufExpanded, { ufExpanded = it },
                cidade, { cidade = it },
                cities, cidadeExpanded, { cidadeExpanded = it },
                bairro, { bairro = it },
                rua, { rua = it },
                numero, { numero = it },
                complemento, { complemento = it },
                pacotes, { pacotes = it }
            )

            SubmitButton() {
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

                if (isEditing) viewModel.updateDelivery(entrega)
                else viewModel.saveDelivery(entrega)

                onBack()
            }
        }
    }
}

@Composable
fun DeliveryFormFields(
    nome: String, onNomeChange: (String) -> Unit,
    cpf: String, onCpfChange: (String) -> Unit,
    data: String, onDataChange: (String) -> Unit,
    cep: String, onCepChange: (String) -> Unit,
    uf: String, onUfSelect: (String) -> Unit, estados: Array<String>,
    ufExpanded: Boolean, onUfExpandedChange: (Boolean) -> Unit,
    cidade: String, onCidadeChange: (String) -> Unit,
    cities: List<String>, cidadeExpanded: Boolean, onCidadeExpandedChange: (Boolean) -> Unit,
    bairro: String, onBairroChange: (String) -> Unit,
    rua: String, onRuaChange: (String) -> Unit,
    numero: String, onNumeroChange: (String) -> Unit,
    complemento: String, onComplementoChange: (String) -> Unit,
    pacotes: String, onPacotesChange: (String) -> Unit
) {
    OutlinedTextField(value = nome, onValueChange = onNomeChange,
        label = { Text(stringResource(R.string.label_name)) })

    OutlinedTextField(value = cpf, onValueChange = onCpfChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(R.string.label_cpf)) })

    OutlinedTextField(value = data, onValueChange = onDataChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(R.string.label_data)) })

    OutlinedTextField(value = cep, onValueChange = onCepChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(R.string.label_cep)) })

    UfDropdown(uf, estados, ufExpanded, onUfExpandedChange, onUfSelect)
    CityDropdown(cidade, cities, cidadeExpanded, onCidadeExpandedChange, onCidadeChange)

    OutlinedTextField(value = bairro, onValueChange = onBairroChange,
        label = { Text(stringResource(R.string.label_neighborhood)) })

    OutlinedTextField(value = rua, onValueChange = onRuaChange,
        label = { Text(stringResource(R.string.label_street)) })

    OutlinedTextField(value = numero, onValueChange = onNumeroChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(stringResource(R.string.label_number)) })

    OutlinedTextField(value = complemento, onValueChange = onComplementoChange,
        label = { Text(stringResource(R.string.label_complement)) })

    OutlinedTextField(value = pacotes, onValueChange = onPacotesChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.label_package)) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UfDropdown(
    uf: String,
    estados: Array<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelected: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value = uf,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.label_uf)) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            estados.forEach { estado ->
                DropdownMenuItem(
                    text = { Text(estado) },
                    onClick = {
                        onSelected(estado)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDropdown(
    city: String,
    cities: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelected: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.label_city)) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            cities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        onSelected(city)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(stringResource(R.string.label_btn_save))
    }
}
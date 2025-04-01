package com.dhafinrazaqa0030.asesmen1mobpro1.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dhafinrazaqa0030.asesmen1mobpro1.R
import com.dhafinrazaqa0030.asesmen1mobpro1.navigation.Screen
import com.dhafinrazaqa0030.asesmen1mobpro1.ui.theme.AplikasiKonversiMataUangTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.about),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    val mataUang = listOf("IDR", "USD", "EUR", "JPY")

    var jumlahUang by remember { mutableStateOf("") }
    var jumlahUangError by remember { mutableStateOf(false) }

    var dariMataUang by remember { mutableStateOf<String?>(null) }
    var dariMataUangError by remember { mutableStateOf(false) }

    var keMataUang by remember { mutableStateOf<String?>(null) }
    var keMataUangError by remember { mutableStateOf(false) }

    var mataUangSamaError by remember { mutableStateOf(false) }

    var expandedFrom by remember { mutableStateOf(false) }
    var expandedTo by remember { mutableStateOf(false) }

    var hasilKonversi by remember { mutableFloatStateOf(0f) }
    var simbolPertama by remember { mutableIntStateOf(0) }
    var simbolKedua by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.currency_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = jumlahUang,
            onValueChange = { jumlahUang = it },
            label = { Text(text = stringResource(R.string.amount)) },
            trailingIcon = {
                IconPicker(jumlahUangError, dariMataUang ?: "")
            },
            supportingText = { ErrorHint(jumlahUangError) },
            isError = jumlahUangError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        CurrencyDropdown(
            label = stringResource(R.string.from_currency),
            selectedCurrency = dariMataUang,
            onCurrencySelected = { dariMataUang = it },
            expanded = expandedFrom,
            onExpandedChange = { expandedFrom = it },
            currencyOptions = mataUang,
            isError = dariMataUangError,
            sameCurrencyError = mataUangSamaError
        )
        CurrencyDropdown(
            label = stringResource(R.string.to_currency),
            selectedCurrency = keMataUang,
            onCurrencySelected = { keMataUang = it },
            expanded = expandedTo,
            onExpandedChange = { expandedTo = it },
            currencyOptions = mataUang,
            isError = keMataUangError,
            sameCurrencyError = mataUangSamaError
        )
        Button(
            onClick = {
                jumlahUangError = (jumlahUang == "" || jumlahUang == "0")
                dariMataUangError = (dariMataUang == null)
                keMataUangError = (keMataUang == null)
                mataUangSamaError = (dariMataUang == keMataUang)

                if (jumlahUangError || dariMataUangError || keMataUangError || mataUangSamaError) return@Button

                hasilKonversi = konversiUang(
                    jumlahUang.toFloatOrNull() ?: 0f,
                    dariMataUang ?: "",
                    keMataUang ?: ""
                ) ?: 0f
                simbolPertama = getSymbol(dariMataUang ?: "")
                simbolKedua = getSymbol(keMataUang ?: "")
            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.convert))
        }
        if (hasilKonversi != 0f) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            Text(
                text = stringResource(R.string.result_title),
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = stringResource(
                    R.string.result,
                    stringResource(simbolPertama, jumlahUang.toFloat()),
                    stringResource(simbolKedua, hasilKonversi)
                ),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropdown(
    label: String,
    selectedCurrency: String?,
    onCurrencySelected: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    currencyOptions: List<String>,
    isError: Boolean,
    sameCurrencyError: Boolean,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCurrency ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text(label) },
            trailingIcon = {
                if (isError || sameCurrencyError) {
                    Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            },
            supportingText = {
                when {
                    isError -> Text(stringResource(R.string.input_invalid))
                    sameCurrencyError -> Text(stringResource(R.string.same_currency_error))
                }
            },
            isError = isError || sameCurrencyError,
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            currencyOptions.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(text = currency) },
                    onClick = {
                        onCurrencySelected(currency)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

private fun konversiUang(jumlah: Float, dari: String, ke: String): Float? {
    val rates = mapOf(
        "IDR" to mapOf("USD" to 0.00006039f, "EUR" to 0.00005592f, "JPY" to 0.009046f),
        "USD" to mapOf("IDR" to 16560f, "EUR" to 0.9259f, "JPY" to 149.8f),
        "EUR" to mapOf("IDR" to 17890f, "USD" to 1.080f, "JPY" to 161.8f),
        "JPY" to mapOf("IDR" to 110.6f, "USD" to 0.006678f, "EUR" to 0.006180f)
    )
    return rates[dari]?.get(ke)?.times(jumlah)
}

private fun getSymbol(mataUang: String): Int {
    return when (mataUang) {
        "IDR" -> R.string.rupiah
        "USD" -> R.string.dollar
        "EUR" -> R.string.euro
        "JPY" -> R.string.yen
        else -> 0
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.input_invalid))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    AplikasiKonversiMataUangTheme {
        MainScreen(rememberNavController())
    }
}
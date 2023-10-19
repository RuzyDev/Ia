package br.com.reconhecimento.ui.reconhecimento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.reconhecimento.data.model.Fonte
import br.com.reconhecimento.data.model.Letras
import br.com.reconhecimento.ui.components.ReconhecimentoDropdownMenu

@Composable
fun ReconhecimentoLetrasRoute() {
    ReconhecimentoLetrasScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReconhecimentoLetrasScreen() {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Reconhecimento") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                TabTreinar()
            }
        }
    }

}

@Composable
fun TabTreinar() {

    var letraAtual by remember { mutableStateOf(Letras.A) }
    var fonteAtual by remember { mutableStateOf(0) }

    Row {
        letraAtual.fontes.getOrNull(fonteAtual)?.let { fonte ->
            Letra(
                letraAtual,
                fonte,
                { letraAtual = it },
                { fonteAtual = it }
            )
        }
    }
}

@Composable
fun Letra(
    letraAtual: Letras,
    fonte: Fonte,
    setLetrasAtual: (Letras) -> Unit,
    setFonteAtual: (Int) -> Unit
) {

    @Composable
    fun Int.color() =
        if (this > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary


    Row {
        ReconhecimentoDropdownMenu(
            Letras.values().toList(),
            label = "Letras",
            onItemClick = { setLetrasAtual(it) },
            text = letraAtual.name,
            textItem = {
                it.name
            },
            modifier = Modifier.width(100.dp)
        )

        Column {
            fonte.values.forEach { linha ->
                Row {
                    linha.forEach {
                        Box(
                            modifier = Modifier
                                .requiredSize(18.dp)
                                .background(it.color())
                        )
                    }
                }
            }
        }
    }

}
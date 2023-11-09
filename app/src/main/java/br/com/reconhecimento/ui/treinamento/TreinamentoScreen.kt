package br.com.reconhecimento.ui.treinamento

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.reconhecimento.data.model.Fonte
import br.com.reconhecimento.data.model.Letras
import br.com.reconhecimento.ui.components.LinearChart
import br.com.reconhecimento.ui.components.ReconhecimentoDropdownMenu
import br.com.reconhecimento.ui.theme.textoSecundario
import kotlinx.coroutines.launch

@Composable
fun ReconhecimentoLetrasRoute(
    viewModel: ReconhecimentoLetrasViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReconhecimentoLetrasScreen(
        treinar = viewModel::treinar,
        testar = viewModel::testar,
        uiState = uiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ReconhecimentoLetrasScreen(
    treinar: () -> Unit,
    testar: () -> Unit,
    uiState: UiState,
) {

    val pagerState = rememberPagerState()
    val tabs = listOf("Treinar", "Testar")
    val scope = rememberCoroutineScope()

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

        Column(Modifier.padding(innerPadding)) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                indicator = { tabPositions ->
                    tabPositions.getOrNull(pagerState.currentPage)?.let {
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(it),
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { tabIndex, tab ->
                    Tab(
                        selected = pagerState.currentPage == tabIndex,
                        unselectedContentColor = MaterialTheme.colorScheme.onPrimary.textoSecundario(),
                        onClick = { scope.launch { pagerState.animateScrollToPage(tabIndex) } },
                        text = {
                            Text(
                                text = tab,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                pageCount = tabs.size,
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> {
                        TabTreinar(uiState, treinar)
                    }

                    1 -> {
                        TabTeste(uiState, testar)
                    }
                }
            }
        }
    }

}

@Composable
fun TabTreinar(
    uiState: UiState,
    treinar: () -> Unit
) {

    var letraAtual by remember { mutableStateOf(Letras.A) }
    var fonteAtual by remember { mutableStateOf(0) }

    LazyColumn(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ReconhecimentoDropdownMenu(
                        Letras.values().toList(),
                        label = "Letras",
                        onItemClick = { letraAtual = it },
                        text = letraAtual.name,
                        textItem = {
                            it.name
                        },
                        modifier = Modifier.width(100.dp)
                    )
                    ReconhecimentoDropdownMenu(
                        (0..2).toList(),
                        label = "Fonte",
                        onItemClick = { fonteAtual = it },
                        text = (fonteAtual + 1).toString(),
                        textItem = {
                            (it + 1).toString()
                        },
                        modifier = Modifier.width(100.dp)
                    )
                }
                letraAtual.fontes.getOrNull(fonteAtual)?.let { fonte ->
                    LetraCard(fonte)
                }
            }
        }

        if (uiState.erros.isNotEmpty()) {
            item {
                Text(
                    text = "Gráfico de erros",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 16.dp)
                )
                LinearChart(
                    data = uiState.erros,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.9f)
                        .height(100.dp)
                )
            }
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ciclos: ${uiState.ciclos}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = treinar) {
                    Text(text = "Treinar")
                }
            }
        }
    }

}

@Composable
fun TabTeste(
    uiState: UiState,
    testar: () -> Unit
) {


    LazyColumn(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Column {
                uiState.vetorTest.vetorTest.forEachIndexed { indexLinha, linha ->
                    Row {
                        linha.forEachIndexed { index, value ->
                            Box(
                                modifier = Modifier
                                    .clickable {
                                        val currentValue =
                                            uiState.vetorTest.vetorTest[indexLinha][index]
                                        uiState.vetorTest.vetorTest[indexLinha][index] =
                                            if (currentValue < 0) 1 else -1
                                    }
                                    .requiredSize(36.dp)
                                    .background(value.color())
                                    .border(1.dp, MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                            fontWeight = FontWeight.Normal
                        )){
                            append("Resultados: ")
                        }
                        withStyle(MaterialTheme.typography.titleMedium.toSpanStyle()){
                            append(uiState.vetorTest.resultadosTeste.joinToString())
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = testar) {
                    Text(text = "Testar")
                }
            }
        }
    }

}

@Composable
fun LetraCard(fonte: Fonte) {

    Column {
        fonte.values.forEach { linha ->
            Row {
                linha.forEach {
                    Box(
                        modifier = Modifier
                            .requiredSize(12.dp)
                            .background(it.color())
                            .border(1.dp, MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}

@Composable
fun Int.color() =
    if (this > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
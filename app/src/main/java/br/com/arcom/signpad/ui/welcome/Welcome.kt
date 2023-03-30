package br.com.arcom.signpad.ui.welcome


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.arcom.signpad.R
import br.com.arcom.signpad.ui.commons.components.*
import br.com.arcom.signpad.ui.dadosvisitante.navigation.navigateToDadosVisitante


@ExperimentalMaterial3Api
@Composable
fun WelcomeRoute(
    navigateToDadosVisitante: () -> Unit,
    navigateToVisitantesCadastrados: () -> Unit
) {
    WelcomeScreen(
        navigateToDadosVisitante = navigateToDadosVisitante,
        navigateToVisitantesCadastrados = navigateToVisitantesCadastrados
    )
}


@ExperimentalMaterial3Api
@Composable
fun WelcomeScreen(
    navigateToDadosVisitante: () -> Unit,
    navigateToVisitantesCadastrados: () -> Unit
) {
    SignPadScaffold(
        bottomBar = {
            Box(modifier = Modifier.padding(12.dp)) {
                Button(
                    onClick = { navigateToDadosVisitante() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = stringResource(id = R.string.concordar_e_prosseguir))
                }
            }
        },
        topBar = {
            SignPadCenterTopBar(title = stringResource(id = R.string.app_name),
                actions = {
                    IconButton(onClick = { navigateToVisitantesCadastrados() }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Icon search")
                    }
                }
            )
        }
    ) {
        LazyColumn(
            Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.termos),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
        }
    }
}

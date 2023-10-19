package br.com.arcom.signpad.ui.visitantescadastrados


import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.signpad.R
import br.com.arcom.signpad.data.model.LgpdVisitante
import br.com.arcom.signpad.ui.assinatura.navigation.AssinaturaArgs
import br.com.arcom.signpad.ui.commons.components.*
import br.com.arcom.signpad.util.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import java.io.File


@ExperimentalMaterial3Api
@Composable
fun VisitantesCadastradosRoute(
    onBackClick: () -> Unit,
    navigateToVisitantePdf: (Long) -> Unit,
    viewModel: VisitantesCadastradosViewModel = hiltViewModel()
) {
    val observables = viewModel.visitantesCadastradosUiState.collectAsStateWithLifecycle()

    VisitantesCadastradosScreen(
        onBackClick = onBackClick,
        observables = observables.value,
        search = viewModel::search,
        clearMessages = viewModel::clearMessages,
        navigateToVisitantePdf = navigateToVisitantePdf,
    )
}


@OptIn(ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun VisitantesCadastradosScreen(
    onBackClick: () -> Unit,
    observables: VisitantesCadastradosUiState,
    search: (String) -> Unit,
    clearMessages: () -> Unit,
    navigateToVisitantePdf: (Long) -> Unit
) {

    BackHandler() {
        onBackClick()
    }

    var search by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    observables.uiMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            clearMessages()
        }
    }

    SignPadScaffold(
        topBar = {
            SignPadCenterTopBar(
                title = stringResource(id = R.string.app_name),
                onBackClick = onBackClick
            )
        },
        snackbarHost = {
            SwipeDismissSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        refreshing = observables.loading
    ) {
        Column(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SignpadOutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        label = stringResource(id = R.string.pesquise),
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        keyboardActions = KeyboardActions(onDone = {
                            if (search.isNotEmpty()) search(search)
                            keyboard?.hide()
                        }),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                    IconButton(
                        onClick = { if (search.isNotEmpty()) search(search) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Icon search"
                        )
                    }
                }
            }

            LazyColumn(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (observables.visitantes.isNotEmpty()) {
                    items(observables.visitantes) { visitante ->
                        CardVisitante(modifier = Modifier.fillMaxWidth(0.9f), visitante) {
                            navigateToVisitantePdf(visitante.id)
                        }
                    }
                } else {

                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondary
                        ) {
                            Text(
                                text = stringResource(
                                    id = if (observables.search.isNotEmpty()) {
                                        R.string.resultados_nao_encontrados
                                    } else {
                                        R.string.realize_uma_pesquisa
                                    }
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardVisitante(modifier: Modifier = Modifier, visitante: LgpdVisitante, onClick: () -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.secondary,
        onClick = onClick
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = visitante.nomeVisitante,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = visitante.dataAssinatura.formatBrasil(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

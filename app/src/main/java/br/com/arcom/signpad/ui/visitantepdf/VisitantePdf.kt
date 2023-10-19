package br.com.arcom.signpad.ui.visitantepdf


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.signpad.R
import br.com.arcom.signpad.data.model.LgpdVisitante
import br.com.arcom.signpad.data.result.ComposableResult
import br.com.arcom.signpad.ui.commons.components.*
import br.com.arcom.signpad.util.*
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@ExperimentalMaterial3Api
@Composable
fun VisitantePdfRoute(
    onBackClick: () -> Unit,
    viewModel: VisitantePdfViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.refresh(context)}

    VisitantePdfScreen(
        onBackClick = onBackClick,
        uiState = uiState,
        clearMessages = viewModel::clearMessages,
        enviarPdfPorEmail = viewModel::enviarPdfPorEmail,
        refresh = {viewModel.refresh(context)}
    )
}


@OptIn(ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun VisitantePdfScreen(
    onBackClick: () -> Unit,
    uiState: VisitantePdfUiState,
    clearMessages: () -> Unit,
    enviarPdfPorEmail: (String) -> Unit,
    refresh: () -> Unit
) {

    BackHandler(onBack = onBackClick)

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var dialogCompartilhar by remember { mutableStateOf(false) }

    if (dialogCompartilhar) {
        DialogCompartilharEmail(closeDialog = { dialogCompartilhar = false }) {
            enviarPdfPorEmail(it)
        }
    }

    uiState.uiMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            clearMessages()
        }
    }

    SignPadScaffold(
        topBar = {
            SignPadCenterTopBar(
                title = stringResource(id = R.string.app_name),
                actions = {
                    IconButton(onClick = refresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Icon atualizar"
                        )
                    }
                    IconButton(onClick = { dialogCompartilhar = true }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Icon compartilhar"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SwipeDismissSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            uiState.pdf.ComposableResult {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    color = Color.White
                ) {
                    AsyncImage(
                        model = it, contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}


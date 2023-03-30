package br.com.arcom.signpad.ui.pdf


import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.signpad.R
import br.com.arcom.signpad.ui.commons.components.*
import br.com.arcom.signpad.util.ImageRotationUtil
import br.com.arcom.signpad.util.MaskVisualTransformation
import br.com.arcom.signpad.util.getImageUri
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import java.io.File


@ExperimentalMaterial3Api
@Composable
fun PdfRoute(
    navigateToWelcome: () -> Unit,
    viewModel: PdfViewModel = hiltViewModel()
) {
    val pdfUiState = viewModel.pdfUiState.collectAsStateWithLifecycle()

    PdfScreen(
        navigateToWelcome = navigateToWelcome,
        pdfUiState = pdfUiState.value,
        clearMessages = viewModel::clearMessages,
        enviarPdfPorEmail = viewModel::enviarPdfPorEmail,
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalMaterial3Api
@Composable
fun PdfScreen(
    navigateToWelcome: () -> Unit,
    pdfUiState: PdfUiState,
    clearMessages: () -> Unit,
    enviarPdfPorEmail: (String) -> Unit,
) {

    BackHandler() {}

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var dialogCompartilhar by remember { mutableStateOf(false) }


    if (dialogCompartilhar) {
        DialogCompartilharEmail(closeDialog = { dialogCompartilhar = false }) {
            enviarPdfPorEmail(it)
        }
    }

    pdfUiState.uiMessage?.let { message ->
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
                    if (pdfUiState.cpf != 0L) {
                        IconButton(onClick = { dialogCompartilhar = true }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Icon compartilhar"
                            )
                        }
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
        },
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Button(
                    onClick = { navigateToWelcome() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = stringResource(id = R.string.finalizar))
                }
            }
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                color = Color.White
            ) {
                AsyncImage(
                    model = pdfUiState.pdf, contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

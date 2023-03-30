package br.com.arcom.signpad.ui.assinatura

import android.content.Context
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.signpad.R
import br.com.arcom.signpad.ui.commons.components.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView


@ExperimentalMaterial3Api
@Composable
fun AssinaturaRoute(
    onBackClick: () -> Unit,
    navigateToPdf: (String, Long) -> Unit,
    viewModel: AssinaturaViewModel = hiltViewModel()
) {
    val observables = viewModel.assinaturaUiState.collectAsStateWithLifecycle()

    AssinaturaScreen(
        onBackClick = onBackClick,
        gerarPdf = viewModel::gerarPdf,
        navigateToPdf = navigateToPdf,
        observables = observables.value,
        clearMessages = viewModel::clearMessages,
        emitMessage = viewModel::emitMessage,
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalMaterial3Api
@Composable
fun AssinaturaScreen(
    onBackClick: () -> Unit,
    navigateToPdf: (String, Long) -> Unit,
    gerarPdf: (Bitmap, Context, (String) -> Unit) -> Unit,
    observables: AssinaturaUiState,
    clearMessages: () -> Unit,
    emitMessage: (String) -> Unit,
) {

    BackHandler() {
        onBackClick()
    }

    val context = LocalContext.current
    var signaturePadAdapter by remember { mutableStateOf<SignaturePadAdapter?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    var pdfPath by remember { mutableStateOf<String?>(null) }

    observables.uiMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            clearMessages()
        }
    }

    LaunchedEffect(Unit){
        delay(200)
        signaturePadAdapter?.clear()
    }

    ScaffoldWithAnimationSuccess(
        bottomBar = {
            Box(modifier = Modifier.padding(12.dp)) {
                Button(
                    onClick = {
                        if (signaturePadAdapter != null) {
                            gerarPdf(signaturePadAdapter!!.getSignatureBitmap(), context) {
                                pdfPath = it
                            }
                        } else {
                            emitMessage("Realize a assinatura!")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = stringResource(id = R.string.confirmar))
                }
            }
        },
        topBar = {
            SignPadCenterTopBar(
                title = stringResource(id = R.string.app_name),
                onBackClick = onBackClick
            )
        }, snackbarHost = {
            SwipeDismissSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        loading = observables.loading,
        success = pdfPath != null,
        navigationSuccess = {
            if (pdfPath != null) navigateToPdf(pdfPath!!, observables.assinaturaArgs?.cpf ?: 0)
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.assinatura),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = { signaturePadAdapter?.clear() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.limpar_assinatura),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                color = Color.White
            ) {
                SignaturePadView(
                    onReady = {
                        signaturePadAdapter = it
                    }, modifier = Modifier.fillMaxSize(),
                    penColor = Color.Black
                )
            }
        }
    }
}

package br.com.arcom.signpad.ui.dadosvisitante


import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.signpad.R
import br.com.arcom.signpad.ui.assinatura.navigation.AssinaturaArgs
import br.com.arcom.signpad.ui.commons.components.*
import br.com.arcom.signpad.util.ImageRotationUtil
import br.com.arcom.signpad.util.MaskVisualTransformation
import br.com.arcom.signpad.util.asNumber
import br.com.arcom.signpad.util.getImageUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.io.File


@ExperimentalMaterial3Api
@Composable
fun DadosVisitanteRoute(
    onBackClick: () -> Unit,
    navigateToAssinatura: (AssinaturaArgs) -> Unit,
    viewModel: DadosVisitanteViewModel = hiltViewModel()
) {
    val fields = viewModel.fields.collectAsStateWithLifecycle()
    val observables = viewModel.dadosVisitanteUiState.collectAsStateWithLifecycle()

    DadosVisitanteScreen(
        onBackClick = onBackClick,
        fields = fields.value,
        navigateToAssinatura = navigateToAssinatura,
        observables = observables.value,
        validarDados = viewModel::validarDados,
        clearMessages = viewModel::clearMessages,
    )
}


@OptIn(ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class)
@ExperimentalMaterial3Api
@Composable
fun DadosVisitanteScreen(
    onBackClick: () -> Unit,
    fields: DadosVisitanteFields,
    navigateToAssinatura: (AssinaturaArgs) -> Unit,
    observables: DadosVisitanteUiState,
    validarDados: ((AssinaturaArgs) -> Unit) -> Unit,
    clearMessages: () -> Unit
) {

    BackHandler() {
        onBackClick()
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var file by remember { mutableStateOf<Pair<File, Uri>?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    observables.uiMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            clearMessages()
        }
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (file != null && success) {
                fields.foto = ImageRotationUtil.rotateAndCompressImage(file!!.first)
            }
        }
    )

    fun startCamera() {
        scope.launch {
            file = getImageUri(context)
            cameraLauncher.launch(file!!.second)
        }
    }

    val permissions = rememberPermissionState(
        Manifest.permission.CAMERA,
        onPermissionResult = {
            if (it) {
                startCamera()
            }
        }
    )

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
        }
    ) {
        LazyColumn(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                PickPhoto(modifier = Modifier.size(150.dp), imagem = fields.foto) {
                    if (permissions.status.isGranted) {
                        startCamera()
                    } else {
                        permissions.launchPermissionRequest()
                    }
                }
            }

            item {
                SignpadOutlinedTextField(
                    value = fields.nome,
                    onValueChange = { fields.nome = it },
                    label = stringResource(id = R.string.nome),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 16.dp),
                    isError = fields.nomeErro,
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                SignpadOutlinedTextField(
                    value = fields.cpf,
                    onValueChange = { if (it.asNumber()) fields.cpf = it.take(11) },
                    label = stringResource(id = R.string.cpf),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = MaskVisualTransformation("###.###.###-##"),
                    isError = fields.cpfErro,
                    keyboardActions = KeyboardActions(onDone = {
                        keyboard?.hide()
                    })
                )

                Button(
                    onClick = {
                        validarDados {
                            keyboard?.hide()
                            navigateToAssinatura(
                                it
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(0.9f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = stringResource(id = R.string.confirmar))
                }
            }
        }
    }
}

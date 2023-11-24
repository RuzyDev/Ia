package br.com.reconhecimento.ui.conta

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.reconhecimento.ui.components.AnimationAcertou
import br.com.reconhecimento.ui.components.AnimationErrou
import br.com.reconhecimento.ui.components.LoadingAnimation
import br.com.reconhecimento.ui.theme.divider
import br.com.reconhecimento.ui.theme.lightColor
import br.com.reconhecimento.util.ImageRotationUtil
import br.com.reconhecimento.util.formatCasasDecimais
import br.com.reconhecimento.util.getImageUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ContasRoute(
    viewModel: ContasViewModel = hiltViewModel()
) {
    val uiState by viewModel.contaUiState.collectAsStateWithLifecycle()

    ContasScreen(
        uiState = uiState,
        recognizeText = viewModel::recognizeText,
        clearAll = viewModel::clearAll,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class
)
@Composable
private fun ContasScreen(
    uiState: ContasResults,
    recognizeText: (Bitmap, () -> Unit) -> Unit,
    clearAll: () -> Unit
) {

    val permission = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var file by remember { mutableStateOf<Pair<File, Uri>?>(null) }
    var acertou by remember { mutableStateOf<Boolean?>(null) }
    var resultVisible by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var bitmapFoto by remember { mutableStateOf<Bitmap?>(null) }
    val keyboard = LocalSoftwareKeyboardController.current


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (file != null && success) {
                val bitmap = ImageRotationUtil.rotateAndCompressImage(file!!.first)
                recognizeText(bitmap) {
                    bitmapFoto = bitmap
                }
            }
        }
    )

    fun startCamera() {
        scope.launch {
            file = getImageUri(context)
            cameraLauncher.launch(file!!.second)
        }
    }

    LaunchedEffect(uiState.uiMessage){
        uiState.uiMessage?.let {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            clearAll()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Photo Math") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    Modifier.fillMaxWidth(0.9f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tire a foto de sua expressão numérica:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = {
                        if (permission.status.isGranted) {
                            startCamera()
                        } else {
                            permission.launchPermissionRequest()
                        }
                    }) {
                        Text(text = "Tirar")
                    }
                }
            }
            item {
                AnimatedVisibility(visible = bitmapFoto?.asImageBitmap() != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        bitmapFoto?.asImageBitmap()?.let {
                            Image(
                                bitmap = it, contentDescription = "", modifier = Modifier
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                        RoundedCornerShape(12.dp)
                                    )
                            )
                        }
                        uiState.expressao?.let {
                            Text(
                                text = "Expressão numérica: ${it.conta}",
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(vertical = 12.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Divider(color = MaterialTheme.colorScheme.onBackground.divider())
                    }
                }
            }
            item {
                Crossfade(acertou, label = "") {
                    when (it) {
                        null -> {
                            if (bitmapFoto != null) {
                                Row(
                                    Modifier.fillMaxWidth(0.9f),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!resultVisible) {
                                        OutlinedTextField(
                                            value = text,
                                            onValueChange = {
                                                val double = it.toDoubleOrNull()
                                                if (double != null) {
                                                    text = it
                                                }
                                            },
                                            label = { Text("Coloque o resultado") },
                                            keyboardOptions = KeyboardOptions(
                                                imeAction = ImeAction.Done,
                                                keyboardType = KeyboardType.Decimal
                                            ),
                                            singleLine = true,
                                            modifier = Modifier
                                                .weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                        )
                                    } else {
                                        uiState.expressao?.let {
                                            Text(
                                                text = "Resultado: ${
                                                    it.resultado.formatCasasDecimais(
                                                        2
                                                    )
                                                }",
                                                modifier = Modifier
                                                    .weight(1f),
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            resultVisible = !resultVisible
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary,
                                            disabledContainerColor = MaterialTheme.colorScheme.primary.lightColor(),
                                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (resultVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Visibility"
                                        )
                                    }
                                    if (!resultVisible) {
                                        IconButton(
                                            onClick = {
                                                acertou = acertouResultado(
                                                    text.toDouble(),
                                                    uiState.expressao?.resultado ?: 0.0
                                                )
                                                keyboard?.hide()
                                            },
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                                disabledContainerColor = MaterialTheme.colorScheme.primary.lightColor(),
                                                disabledContentColor = MaterialTheme.colorScheme.onPrimary
                                            ),
                                            enabled = text.toDoubleOrNull() != null
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = Icons.Default.Check.name
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        true -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                AnimationAcertou()
                                uiState.expressao?.let {
                                    Text(
                                        text = "Resultado: ${it.resultado.formatCasasDecimais(2)}",
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .padding(top = 12.dp),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }

                        }

                        false -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                AnimationErrou()
                                uiState.expressao?.let {
                                    Text(
                                        text = "Resultado: ${it.resultado.formatCasasDecimais(2)}",
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .padding(top = 12.dp),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }

        LoadingScreen(uiState.loading)
    }

}

fun acertouResultado(resultado: Double, usuario: Double): Boolean {
    val tolerancia = 0.001
    val resultadoString = removerZerosDireita(resultado)
    val usuarioString = removerZerosDireita(usuario)
    return Math.abs(resultadoString.toDouble() - usuarioString.toDouble()) < tolerancia
}


fun removerZerosDireita(numero: Double): String {
    val numeroComoString = numero.toString()
    val numeroSemZeros = numeroComoString.trimEnd('0')

    return if (numeroSemZeros.endsWith('.')) {
        numeroSemZeros.substring(0, numeroSemZeros.length - 1)
    } else {
        numeroSemZeros
    }
}

@Composable
fun LoadingScreen(enabled: Boolean) {

    val interactionSource = remember { MutableInteractionSource() }

    if (enabled) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(0.5f))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {}
        ) {

            LoadingAnimation(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}



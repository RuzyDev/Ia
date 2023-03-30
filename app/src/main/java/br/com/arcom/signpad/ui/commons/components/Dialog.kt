package br.com.arcom.signpad.ui.commons.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.arcom.signpad.R

@Composable
fun DialogConfirmacao(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    title: String? = null,
    text: @Composable (() -> Unit)? = null,
    icon: ImageVector? = null,
    closeDialog: () -> Unit,
    confirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { closeDialog() },
        title = { if (title != null) Text(text = title) },
        confirmButton = {
            TextButton(
                onClick = {
                    confirmClick()
                    closeDialog()
                },
            ) {
                Text(
                    text = "confirmar",
                    color = contentColor
                )
            }
        },
        text = if (text != null) {
            { text() }
        } else null,
        dismissButton = {
            TextButton(
                onClick = { closeDialog() }
            ) {
                Text(
                    text = "cancelar",
                    color = contentColor
                )
            }
        },
        containerColor = containerColor,
        modifier = modifier,
        icon = if (icon != null) {
            {
                Icon(
                    imageVector = icon, contentDescription = "Icon",
                    tint = contentColor
                )
            }
        } else null
    )
}

@Composable
fun DialogCompartilharEmail(
    closeDialog: () -> Unit,
    enviar: (String) -> Unit
) {
    var error by remember {
        mutableStateOf(false)
    }
    var email by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = { closeDialog() },
        title = {
            Text(text = stringResource(id = R.string.compartilhar))
        },
        text = {
            LazyColumn(Modifier.fillMaxWidth()) {
                item {
                    Text(
                        text = stringResource(id = R.string.digite_email),
                        Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                    )
                }
                item {
                    SignpadOutlinedTextField(
                        onValueChange = { email = it }, value = email,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        label = stringResource(id = R.string.email),
                        isError = error,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (email.isNotEmpty()) {
                        enviar(email)
                        closeDialog()
                    } else {
                        error = true
                    }
                },
            ) {
                Text(
                    stringResource(id = R.string.confirmar),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { closeDialog() },
            ) {
                Text(
                    stringResource(id = R.string.cancelar),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.secondary,
        iconContentColor = MaterialTheme.colorScheme.onSecondary,
        icon = {
            Icon(
                imageVector = Icons.Default.Share, contentDescription = "Ícone compartilhar",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}


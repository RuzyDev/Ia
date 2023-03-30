package br.com.arcom.signpad.ui.commons.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import br.com.arcom.signpad.ui.commons.shake
import br.com.arcom.signpad.ui.theme.SignpadRoundedShape
import br.com.arcom.signpad.ui.theme.lightColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignpadOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {

    var error by remember { mutableStateOf(false) }

    LaunchedEffect(isError){
        if (isError) {
            error = true
            delay(200)
            error = false
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        modifier = modifier.shake(error),
        keyboardOptions = keyboardOptions,
        label = { Text(text = label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = containerColor,
            focusedSupportingTextColor = contentColor,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = contentColor.lightColor(),
            focusedLabelColor = contentColor
        ),
        keyboardActions = keyboardActions,
        shape = SignpadRoundedShape,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        isError = isError
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignpadTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError:Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.background,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {

    var error by remember { mutableStateOf(false) }

    LaunchedEffect(isError){
        if (isError) {
            error = true
            delay(200)
            error = false
        }
    }

    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        modifier = modifier.shake(error),
        keyboardOptions = keyboardOptions,
        label = { Text(text = label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = containerColor,
            focusedSupportingTextColor = MaterialTheme.colorScheme.onBackground,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.lightColor(),
            focusedLabelColor = MaterialTheme.colorScheme.onBackground
        ),
        keyboardActions = keyboardActions,
        shape = SignpadRoundedShape,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        isError = isError
    )
}
package br.com.reconhecimento.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.reconhecimento.ui.theme.divider
import br.com.reconhecimento.ui.theme.lightColor
import br.com.reconhecimento.ui.theme.textoSecundario

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun <T> ReconhecimentoDropdownMenu(
    items: List<T>,
    onItemClick: (item: T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    dismissOnItemClick: Boolean = true,
    label: String,
    text: String,
    textItem: @Composable (item: T) -> String,
    itemLeadingIcon: @Composable ((item: T) -> Unit)? = null,
    itemTrailingIcon: @Composable ((item: T) -> Unit)? = null,
    isError: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
) {


    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedButton(
        onClick = { expanded = !expanded }, shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(
            1.dp,
            if (isError) MaterialTheme.colorScheme.error else contentColor.lightColor()
        ),
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
        enabled = enabled
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    if (text.isNotEmpty()) {
                        withStyle(SpanStyle(color = contentColor)) {
                            append(text)
                        }
                    } else {
                        withStyle(SpanStyle(color = contentColor.textoSecundario())) {
                            append(label)
                        }
                    }
                },
                style = LocalTextStyle.current,
                modifier = Modifier.weight(1f)
            )
            Icon(
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                "contentDescription"
            )
        }
    }


    if (expanded) {
        Dialog(
            onDismissRequest = {
                expanded = false
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth(0.9f)) {
                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleSmall,
                        color = contentColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Divider(color = contentColor.divider())
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        itemsIndexed(items) { index, item ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = textItem(item),
                                        color = contentColor
                                    )
                                },
                                onClick = {
                                    onItemClick(item)
                                    if (dismissOnItemClick) expanded = false
                                },
                                leadingIcon = if (itemLeadingIcon != null) {
                                    { itemLeadingIcon(item) }
                                } else {
                                    null
                                },
                                trailingIcon = if (itemTrailingIcon != null) {
                                    { itemTrailingIcon(item) }
                                } else {
                                    null
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = contentColor
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

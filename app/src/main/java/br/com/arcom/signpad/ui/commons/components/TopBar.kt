package br.com.arcom.signpad.ui.commons.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignPadTopBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: (@Composable () -> Unit)? = null,
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
    ),
    onBackClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        },
        actions = {
            if (actions != null) {
                actions()
            }
        },
        colors = colors,
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector =  Icons.Default.ArrowBack,
                        contentDescription = "Icone voltar"
                    )
                }
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignPadCenterTopBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: (@Composable () -> Unit)? = null,
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
    ),
    onBackClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        },
        actions = {
            if (actions != null) {
                actions()
            }
        },
        colors = colors,
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Icone voltar"
                    )
                }
            }
        },
        modifier = modifier
    )
}
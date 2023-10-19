package br.com.reconhecimento.ui.app

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import br.com.reconhecimento.ui.navigation.ReconhecimentoNavHost
import br.com.reconhecimento.ui.theme.ReconhecimentoTheme

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun SignpadApp(
    windowSizeClass: WindowSizeClass,
    appState: ReconhecimentoAppState = rememberReconhecimentoAppState(
        windowSizeClass = windowSizeClass
    ),
) {
    ReconhecimentoTheme() {

        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier.semantics {
                testTagsAsResourceId = true
            },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            ReconhecimentoNavHost(
                navController = appState.navController,
                onBackClick = appState::onBackClick,
                onBackClickWithDestination = appState::onBackClickWithDestination,
                modifier = Modifier.padding(padding)
            )
        }

    }
}


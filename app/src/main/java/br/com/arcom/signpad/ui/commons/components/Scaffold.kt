package br.com.arcom.signpad.ui.commons.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import br.com.arcom.signpad.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SignPadScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    loadingScreen: Boolean = false,
    onRefresh: () -> Unit = {},
    refreshing: Boolean = false,
    content: @Composable () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefresh)

    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = contentWindowInsets
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            content()

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
    LoadingScreen(loadingScreen)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithAnimationSuccess(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    loading: Boolean,
    success: Boolean,
    navigationSuccess: () -> Unit,
    content: @Composable () -> Unit
) {
    Crossfade(success) { s ->
        if (!s) {
            Scaffold(
                modifier = modifier,
                topBar = topBar,
                bottomBar = bottomBar,
                snackbarHost = snackbarHost,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = contentWindowInsets
            ) {
                Box(Modifier.padding(it)) {
                    content()
                    LoadingScreen(enabled = loading)
                }
            }
        } else {
            EnvioAnimationSucess(
                starAnimation = true,
                funcaoSucesso = {
                    navigationSuccess()
                }
            )
        }
    }
}

@Composable
fun EnvioAnimationSucess(
    starAnimation: Boolean,
    funcaoSucesso: () -> Unit,
) {

    var speed by remember {
        mutableStateOf(2f)
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.check)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = starAnimation,
        speed = speed,
        restartOnPlay = false
    )

    Box(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .fillMaxSize(0.5f)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
    }

    if (progress == 1f) {
        funcaoSucesso()
    }
}

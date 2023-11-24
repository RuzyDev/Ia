package br.com.reconhecimento.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.reconhecimento.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun Animation(
    modifier: Modifier = Modifier, rawDrawable: Int, startAnimation: Boolean,
    functionAfterAnimationComplete: (() -> Unit)? = null
) {
    var speed by remember {
        mutableStateOf(2f)
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(rawDrawable)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = startAnimation,
        speed = speed,
        restartOnPlay = false
    )

    LottieAnimation(
        composition,
        progress,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
    if (progress == 1f) {
        if (functionAfterAnimationComplete != null) {
            functionAfterAnimationComplete()
        }
    }
}

@Composable
fun InfiniteAnimation(modifier: Modifier = Modifier, rawDrawable: Int, startAnimation: Boolean) {
    var speed by remember {
        mutableStateOf(1f)
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(rawDrawable)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = startAnimation,
        speed = speed,
        restartOnPlay = false

    )

    LottieAnimation(
        composition,
        progress,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}


@Composable
fun AnimationAcertou(
    text: String = stringResource(id = R.string.voce_acertou),
    size: Dp = 72.dp
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfiniteAnimation(
            modifier = Modifier
                .requiredSize(size),
            R.raw.check, true
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AnimationErrou(
    text: String = stringResource(id = R.string.voce_errou),
    size: Dp = 72.dp
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfiniteAnimation(
            modifier = Modifier
                .requiredSize(size),
            R.raw.error, true
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
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
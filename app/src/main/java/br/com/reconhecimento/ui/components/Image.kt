package br.com.reconhecimento.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.reconhecimento.R
import coil.compose.AsyncImage

@Composable
fun ImageComponent(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Crop,
) {
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.Center) {
        AsyncImage(model = model,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            onLoading = {
                error = false
                loading = true
            },
            onSuccess = {
                error = false
                loading = false
            },
            onError = {
                loading = false
                error = true
            })
        if (loading) {
            LoadingAnimation(size = 24.dp)
        }
        if (error) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .requiredSize(24.dp),
                tint = MaterialTheme.colorScheme.primary,
                imageVector = Icons.Default.ImageNotSupported,
                contentDescription = ""
            )
        }
    }

}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    Box(modifier = modifier.requiredSize(size)) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo app",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .requiredSize(size / 2)
                .align(Alignment.Center)
        )
    }
}
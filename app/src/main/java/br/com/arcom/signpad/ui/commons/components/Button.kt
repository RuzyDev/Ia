package br.com.arcom.signpad.ui.commons.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.arcom.signpad.ui.theme.lightColor
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PickPhoto(
    modifier: Modifier = Modifier,
    imagem: Any?,
    onClick: () -> Unit,
) {
    var loading by remember { mutableStateOf(false) }

    Surface(
        onClick = {
            onClick()
        },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.secondary
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imagem)
                .build(),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "Selected image",
            contentScale = ContentScale.Crop,
            onLoading = {
                loading = true
            },
            onSuccess = {
                loading = false
            }
        )

        if (imagem == null) {
            Icon(
                imageVector = Icons.Filled.NoPhotography,
                contentDescription = "Imagem cliente",
                modifier = Modifier.requiredSize(50.dp),
                tint = MaterialTheme.colorScheme.onBackground.lightColor()
            )
        }

        if (loading){
            Box(Modifier.fillMaxSize()) {
                LoadingAnimation(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
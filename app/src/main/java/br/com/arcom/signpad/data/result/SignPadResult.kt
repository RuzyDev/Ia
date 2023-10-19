package br.com.arcom.signpad.data.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.arcom.signpad.R
import br.com.arcom.signpad.ui.commons.components.AnimationError
import br.com.arcom.signpad.ui.commons.components.AnimationNaoEncontrado
import br.com.arcom.signpad.ui.commons.components.LoadingAnimation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface SignPadResult<out T> {
    data class Success<T>(val data: T) : SignPadResult<T>
    data class Error(val exception: Throwable? = null) : SignPadResult<Nothing>
    object Loading : SignPadResult<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<SignPadResult<T>> {
    return this
        .map<T, SignPadResult<T>> {
            SignPadResult.Success(it)
        }
        .onStart { emit(SignPadResult.Loading) }
        .catch { emit(SignPadResult.Error(it)) }
}

@Composable
fun <T> SignPadResult<out T>.ComposableResult(
    textError: String = stringResource(id = R.string.ocorreu_um_erro),
    emptyText: String = stringResource(id = R.string.dados_nao_encontrados),
    success: @Composable (T) -> Unit
) {
    when (this) {
        is SignPadResult.Success -> when (this.data) {
            is List<*> -> {
                if (this.data.isEmpty()) {
                    AnimationNaoEncontrado(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        text = emptyText
                    )
                } else {
                    success(this.data)
                }
            }

            else -> {
                success(this.data)
            }
        }

        is SignPadResult.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation()
            }
        }

        is SignPadResult.Error -> {
            AnimationError(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                size = 80.dp,
                text = textError
            )
        }
    }
}
package br.com.arcom.signpad.ui.visitantepdf

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.repplus.ui.commons.UiMessage
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.signpad.data.domain.FindVisitantePdf
import br.com.arcom.signpad.data.domain.SendEmail
import br.com.arcom.signpad.data.result.SignPadResult
import br.com.arcom.signpad.ui.commons.ObservableLoadingCounter
import br.com.arcom.signpad.ui.commons.collectStatus
import br.com.arcom.signpad.ui.visitantepdf.navigation.VisitantePdfNavigation
import br.com.arcom.signpad.util.Logger
import br.com.arcom.signpad.util.base64ToBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisitantePdfViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val findVisitantePdf: FindVisitantePdf,
    private val sendEmail: SendEmail,
    private val logger: Logger
) : ViewModel() {

    private val loading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val cpf = VisitantePdfNavigation.getArguments(savedStateHandle)

    private val _pdf = MutableStateFlow<SignPadResult<Bitmap?>>(SignPadResult.Loading)

    fun emitMessage(value: String) {
        viewModelScope.launch {
            uiMessageManager.emitMessage(UiMessage(message = value))
        }
    }

    fun clearMessages() {
        viewModelScope.launch {
            uiMessageManager.clearAll()
        }
    }

    fun refresh(context: Context) {
        viewModelScope.launch {
            try {
                loading.addLoader()
                val base64 = findVisitantePdf.executeSync(FindVisitantePdf.Params(cpf))
                _pdf.emit(
                    if (base64 != null) {
                        context.base64ToBitmap(base64).let {
                            if (it != null) SignPadResult.Success(it) else SignPadResult.Error()
                        }
                    } else {
                        SignPadResult.Error()
                    }
                )
                loading.removeLoader()
            } catch (e: Exception) {
                loading.removeLoader()
                emitMessage("Ocorreu um erro!")
            }
        }
    }

    fun enviarPdfPorEmail(email: String) {
        viewModelScope.launch {
            sendEmail(
                SendEmail.Params(
                    email,
                    cpf
                )
            ).collectStatus(
                loading,
                logger,
                uiMessageManager,
                callback = {
                    emitMessage("Enviado com sucesso!")
                }
            )
        }
    }

    val uiState: StateFlow<VisitantePdfUiState> =
        combine(
            _pdf,
            loading.observable,
            uiMessageManager.message,
            ::VisitantePdfUiState
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = VisitantePdfUiState.Empty
        )
}

data class VisitantePdfUiState(
    val pdf: SignPadResult<Bitmap?> = SignPadResult.Loading,
    val loading: Boolean = false,
    val uiMessage: UiMessage? = null
) {
    companion object {
        val Empty = VisitantePdfUiState()
    }
}
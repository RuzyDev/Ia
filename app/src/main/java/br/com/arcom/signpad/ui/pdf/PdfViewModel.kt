package br.com.arcom.signpad.ui.pdf

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.repplus.ui.commons.UiMessage
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.signpad.data.domain.SendEmail
import br.com.arcom.signpad.data.domain.SendVisitante
import br.com.arcom.signpad.ui.assinatura.navigation.AssinaturaNavigation
import br.com.arcom.signpad.ui.commons.ObservableLoadingCounter
import br.com.arcom.signpad.ui.commons.collectStatus
import br.com.arcom.signpad.ui.pdf.navigation.PdfNavigation
import br.com.arcom.signpad.util.Logger
import br.com.arcom.signpad.util.convertPdf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PdfViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val sendEmail: SendEmail,
    private val logger: Logger
) : ViewModel() {

    private val pathPdf: String = checkNotNull(
        savedStateHandle[PdfNavigation.pdfArg]
    )

    private val cpf: Long = checkNotNull(
        savedStateHandle[PdfNavigation.cpfArg]
    )

    private val loading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    private val _pdf = MutableStateFlow<Bitmap?>(null)

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

    val pdfUiState: StateFlow<PdfUiState> =
        combine(_pdf, loading.observable, uiMessageManager.message) { pdf, loading, message ->
            PdfUiState(pdf, loading, message, cpf)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PdfUiState.Empty
        )

    init {
        _pdf.value = convertPdf(File(pathPdf))
    }
}

data class PdfUiState(
    val pdf: Bitmap? = null,
    val loading: Boolean = false,
    val uiMessage: UiMessage? = null,
    val cpf: Long = 0
) {
    companion object {
        val Empty = PdfUiState()
    }
}
package br.com.arcom.signpad.ui.assinatura

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.repplus.ui.commons.UiMessage
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.signpad.data.domain.SendVisitante
import br.com.arcom.signpad.ui.assinatura.navigation.AssinaturaArgs
import br.com.arcom.signpad.ui.assinatura.navigation.AssinaturaNavigation
import br.com.arcom.signpad.ui.commons.ObservableLoadingCounter
import br.com.arcom.signpad.ui.commons.collectStatus
import br.com.arcom.signpad.util.Logger
import br.com.arcom.signpad.util.PdfTermoCompromisso
import br.com.arcom.signpad.util.salvarAssinatura
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssinaturaViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val sendVisitante: SendVisitante,
    private val logger: Logger
) : ViewModel() {

    private val loading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val args: String = checkNotNull(
        savedStateHandle[AssinaturaNavigation.assinaturaArgs]
    )
    val assinaturaArgs = Gson().fromJson(args, AssinaturaArgs::class.java)

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

    fun gerarPdf(bitmap: Bitmap, context: Context, callBack: (String) -> Unit) {
        val titlePdf = "${assinaturaArgs.nome}-${assinaturaArgs.cpf}"
        val pathAssinatura = salvarAssinatura(bitmap, context)
        val pathFoto = salvarAssinatura(assinaturaArgs.foto, context)
        val pathPdf = PdfTermoCompromisso.criarPdf(
            context,
            assinaturaArgs.nome,
            assinaturaArgs.cpf.toString(),
            pathFoto,
            pathAssinatura,
            titlePdf
        )

        viewModelScope.launch {
            sendVisitante(
                SendVisitante.Params(
                    assinaturaArgs.nome,
                    assinaturaArgs.cpf,
                    pathPdf!!
                )
            ).collectStatus(
                loading,
                logger,
                uiMessageManager,
                callback = {
                    callBack(pathPdf)
                }
            )
        }

    }

    val assinaturaUiState: StateFlow<AssinaturaUiState> =
        combine(loading.observable, uiMessageManager.message) { loading, message ->
            AssinaturaUiState(loading, message, assinaturaArgs)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AssinaturaUiState.Empty
        )

}


data class AssinaturaUiState(
    val loading: Boolean = false,
    val uiMessage: UiMessage? = null,
    val assinaturaArgs: AssinaturaArgs? = null
) {
    companion object {
        val Empty = AssinaturaUiState()
    }
}
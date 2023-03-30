package br.com.arcom.signpad.ui.dadosvisitante

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.repplus.ui.commons.UiMessage
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.signpad.ui.assinatura.navigation.AssinaturaArgs
import br.com.arcom.signpad.ui.commons.ObservableLoadingCounter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DadosVisitanteViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val loading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    private val _fields = MutableStateFlow(DadosVisitanteFields())
    val fields = _fields.asStateFlow()

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

    fun validarDados(callBack: (AssinaturaArgs) -> Unit) {
        val fields = _fields.value
        val nomeValido = fields.nome.split(" ").size > 1
        if (fields.nome.isNotEmpty() &&
            fields.cpf.isNotEmpty() &&
            fields.foto != null &&
            nomeValido
        ) {
            callBack(
                AssinaturaArgs(
                    fields.nome,
                    fields.cpf.toLong(),
                    fields.foto!!,
                )
            )
        } else {
            if (fields.foto == null) {
                emitMessage("Insira uma foto!")
            }
            fields.nomeErro = fields.nome.isEmpty() || !nomeValido
            fields.cpfErro = fields.cpf.isEmpty()
        }
    }

    val dadosVisitanteUiState: StateFlow<DadosVisitanteUiState> =
        combine(loading.observable, uiMessageManager.message) { loading, message ->
            DadosVisitanteUiState(loading, message)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DadosVisitanteUiState.Empty
        )
}

class DadosVisitanteFields {
    var foto by mutableStateOf<Bitmap?>(null)
    var nome by mutableStateOf<String>("")
    var nomeErro by mutableStateOf(false)
    var cpf by mutableStateOf<String>("")
    var cpfErro by mutableStateOf(false)
}


data class DadosVisitanteUiState(
    val loading: Boolean = false,
    val uiMessage: UiMessage? = null
) {
    companion object {
        val Empty = DadosVisitanteUiState()
    }
}
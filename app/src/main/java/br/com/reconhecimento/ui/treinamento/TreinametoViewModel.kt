package br.com.reconhecimento.ui.treinamento

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.reconhecimento.data.domain.ObserveErros
import br.com.reconhecimento.data.domain.ObserveUltimoTreino
import br.com.reconhecimento.data.domain.Treinar
import br.com.reconhecimento.data.model.Erro
import br.com.reconhecimento.data.model.Treinos
import br.com.reconhecimento.ui.reconhecimento.UiState
import br.com.reconhecimento.util.ObservableLoadingCounter
import br.com.reconhecimento.util.UiMessage
import br.com.reconhecimento.util.UiMessageManager
import br.com.reconhecimento.util.collectStatus
import br.com.reconhecimento.util.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TreinamentoViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val treinar: Treinar,
    private val observeUltimoTreino: ObserveUltimoTreino,
    private val observeErros: ObserveErros,
) : ViewModel() {

    val loading = ObservableLoadingCounter()
    val message = UiMessageManager()
    private val _fields = MutableStateFlow(Fields())
    val fields = _fields.asStateFlow()


    val uiState = combine(
        message.message,
        loading.observable,
        observeUltimoTreino.flow,
        observeErros.flow,
        ::TreinamentoResults
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TreinamentoResults.Empty,
    )

    fun treinar() {
        val fields = _fields.value
        viewModelScope.launch {
            treinar(
                Treinar.Params(
                    taxaAprendizagem = fields.taxaAprendizagem.toDouble(),
                    numeroDeCiclosDesejados = fields.quantidadeCiclos.toInt(),
                    momento = fields.momento.toDouble(),
                )
            ).collectStatus(
                counter = loading,
                uiMessageManager = message
            )
        }
    }

    init {
        observeUltimoTreino(ObserveUltimoTreino.Params())
        observeErros(ObserveErros.Params())
    }

}

class Fields {
    var taxaAprendizagem by mutableStateOf("")
    var momento by mutableStateOf("")
    var quantidadeCiclos by mutableStateOf("")
}

data class TreinamentoResults(
    val message: UiMessage? = null,
    val loading: Boolean = false,
    val ultimoTreino: List<Treinos> = emptyList(),
    val erros: List<Erro> = emptyList(),
) {
    companion object {
        val Empty = TreinamentoResults()
    }
}
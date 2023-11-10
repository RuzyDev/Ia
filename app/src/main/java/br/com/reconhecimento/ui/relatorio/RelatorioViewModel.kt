package br.com.reconhecimento.ui.relatorio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.reconhecimento.data.domain.GetRelatorio
import br.com.reconhecimento.data.domain.ObserveErros
import br.com.reconhecimento.data.domain.ObserveUltimoTreino
import br.com.reconhecimento.data.domain.Treinar
import br.com.reconhecimento.data.model.Erro
import br.com.reconhecimento.data.model.TreinoRealizado
import br.com.reconhecimento.data.model.Treinos
import br.com.reconhecimento.ui.reconhecimento.UiState
import br.com.reconhecimento.ui.treinamento.TreinamentoResults
import br.com.reconhecimento.util.ObservableLoadingCounter
import br.com.reconhecimento.util.UiMessage
import br.com.reconhecimento.util.UiMessageManager
import br.com.reconhecimento.util.collectStatus
import br.com.reconhecimento.util.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RelatorioViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val treinar: Treinar,
    private val getRelatorio: GetRelatorio
) : ViewModel() {

    val loading = ObservableLoadingCounter()
    val message = UiMessageManager()
    private val _relatorios = MutableStateFlow<List<TreinoRealizado>>(emptyList())


    val uiState: StateFlow<RelatorioResults> = combine(
        message.message,
        loading.observable,
        _relatorios,
        ::RelatorioResults
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RelatorioResults.Empty,
    )

    fun refresh() {
        viewModelScope.launch {
            val relatorios = getRelatorio.executeSync(GetRelatorio.Params())
            _relatorios.emit(relatorios)
        }
    }

    init {
        refresh()
    }

}

class Fields {
    var taxaAprendizagem by mutableStateOf("")
    var momento by mutableStateOf("")
    var quantidadeCiclos by mutableStateOf("")
}

data class RelatorioResults(
    val message: UiMessage? = null,
    val loading: Boolean = false,
    val relatorios: List<TreinoRealizado> = emptyList(),
) {
    companion object {
        val Empty = RelatorioResults()
    }
}
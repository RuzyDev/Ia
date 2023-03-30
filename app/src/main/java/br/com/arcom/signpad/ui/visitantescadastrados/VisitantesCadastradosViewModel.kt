package br.com.arcom.signpad.ui.visitantescadastrados

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.arcom.repplus.ui.commons.UiMessage
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.signpad.data.domain.FindVisitantes
import br.com.arcom.signpad.data.model.LgpdVisitante
import br.com.arcom.signpad.ui.commons.ObservableLoadingCounter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisitantesCadastradosViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val findVisitantes: FindVisitantes
) : ViewModel() {

    private val loading = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    private val _visitantes = MutableStateFlow<List<LgpdVisitante>>(emptyList())
    private val _search = MutableStateFlow("")

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

    fun search(value: String) {
        viewModelScope.launch {
            _search.emit(value)
        }
    }

    fun findVisitantes(value: String) {
        viewModelScope.launch {
            loading.addLoader()
            val visitantes = findVisitantes.executeSync(FindVisitantes.Params(value))
            _visitantes.emit(visitantes)
            loading.removeLoader()
        }
    }

    val visitantesCadastradosUiState: StateFlow<VisitantesCadastradosUiState> =
        combine(
            _search,
            _visitantes,
            loading.observable,
            uiMessageManager.message
        ) { search, visitantes, loading, message ->
            VisitantesCadastradosUiState(search,visitantes, loading, message)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = VisitantesCadastradosUiState.Empty
        )

    init {
        _search.onEach {
            if (it.isNotEmpty()) findVisitantes(it)
        }.launchIn(viewModelScope)
    }
}

data class VisitantesCadastradosUiState(
    val search: String = "",
    val visitantes: List<LgpdVisitante> = emptyList(),
    val loading: Boolean = false,
    val uiMessage: UiMessage? = null
) {
    companion object {
        val Empty = VisitantesCadastradosUiState()
    }
}
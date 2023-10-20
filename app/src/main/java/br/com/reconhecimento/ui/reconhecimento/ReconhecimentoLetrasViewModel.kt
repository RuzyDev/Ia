package br.com.reconhecimento.ui.reconhecimento

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.reconhecimento.data.model.Fonte
import br.com.reconhecimento.data.model.Letras
import br.com.reconhecimento.data.model.defaultPeso
import br.com.reconhecimento.data.model.saidasDesejadas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.pow

@HiltViewModel
class ReconhecimentoLetrasViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val taxaAprendizagem = 0.002
    private val quantidadeMaximaCiclos = 1000
    private val erroMinimo = 0.0001

    val vetorTeste = MutableStateFlow(NovoVetorTeste())
    val erros = MutableStateFlow<List<Double>>(emptyList())

    val pesosTreino = MutableStateFlow(defaultPeso.toList())
    val ciclos = MutableStateFlow(0)
    val stateTreino = MutableStateFlow(States())

    val uiState: StateFlow<UiState> =
        combine(ciclos, vetorTeste, erros, ::UiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Empty,
            )


    fun treinar() {
        viewModelScope.launch(Dispatchers.IO) {
            pesosTreino.emit(defaultPeso.toList())

            val state = stateTreino.value
            val ciclosValue = ciclos.value
            val amostrasTreinamento =
                Letras.values().map { it.fontes.map(Fonte::getValueComBias) }.flatten()
            val pesos = pesosTreino.value.toMutableList()

            while ((abs(state.erroQuadraticoMedioAtual - state.erroQuadraticoMedioAnterior) > erroMinimo) && (ciclosValue < quantidadeMaximaCiclos)) {
                state.somaErroQuadraticoCiclo = 0.0
                state.erroQuadraticoMedioAnterior = state.erroQuadraticoMedioAtual

                amostrasTreinamento.forEachIndexed { indexAmostra, amostra ->
                    saidasDesejadas.forEachIndexed { indexSaida, saida ->
                        var sinapse = 0.0

                        amostra.forEachIndexed { index, value ->
                            sinapse += (value * pesos[indexSaida][index])
                        }

                        state.erro = saida[indexAmostra] - sinapse

                        amostra.forEachIndexed { index, value ->
                            val deltaW = value * state.erro * taxaAprendizagem
                            pesos[indexSaida][index] += deltaW
                        }

                        state.somaErroQuadraticoCiclo += state.erro.pow(2.0)
                    }
                }
                state.erroQuadraticoMedio = state.somaErroQuadraticoCiclo / 21
                state.erroQuadraticoMedioAtual = state.erroQuadraticoMedio

                ciclos.update {
                    it + 1
                }
                erros.update {
                    it.toMutableList().apply {
                        add(abs(state.erroQuadraticoMedio))
                    }
                }
            }
            pesosTreino.emit(pesos)
        }
    }

    fun testar() {
        val valorASerTestado = vetorTeste.value.getValueComBias()
        val resultados = mutableListOf<String>()

        saidasDesejadas.forEachIndexed { index, saida ->
            var resultado = 0.0
            valorASerTestado.forEachIndexed { indexTeste, it ->
                resultado += (it * pesosTreino.value[index][indexTeste])
            }

            if (resultado >= 0) {
                Letras.getByIndex(index)?.name?.let { letra ->
                    resultados.add(letra)
                }
            }
        }

        vetorTeste.value.resultadosTeste.clear()
        vetorTeste.value.resultadosTeste.addAll(resultados.ifEmpty {
            listOf("Não identificado!")
        })
    }
}

class NovoVetorTeste {
    val vetorTest = mutableStateListOf(
        mutableStateListOf(-1, -1, -1, -1, -1, -1, -1),
        mutableStateListOf(-1, -1, -1, -1, -1, -1, -1),
        mutableStateListOf(-1, -1, -1, -1, -1, -1, -1),
        mutableStateListOf(-1, -1, -1, -1, -1, -1, -1),
        mutableStateListOf(-1, -1, -1, -1, -1, -1, -1),
        mutableStateListOf(-1, -1, -1, -1, -1, -1, -1),
        mutableStateListOf(-1, -1, -1, -1, -1, -1, -1),
        mutableStateListOf(-1, -1, -1, -1, -1, -1, -1),
        mutableStateListOf(-1, -1, -1, -1, -1, -1, -1),
    )
    var resultadosTeste = mutableStateListOf("")

    fun getValueComBias(): List<Int> {
        return vetorTest.flatten().toMutableList().apply {
            this.add(1)
        }
    }
}

class States {
    var erroQuadraticoMedio by mutableStateOf(1.0)
    var somaErroQuadraticoCiclo by mutableStateOf(0.0)
    var erroQuadraticoMedioAtual by mutableStateOf(0.0)
    var erroQuadraticoMedioAnterior by mutableStateOf(1.0)
    var erro by mutableStateOf(0.0)
}

data class UiState(
    val ciclos: Int = 0,
    val vetorTest: NovoVetorTeste = NovoVetorTeste(),
    val erros: List<Double> = emptyList(),
) {
    companion object {
        val Empty = UiState()
    }
}
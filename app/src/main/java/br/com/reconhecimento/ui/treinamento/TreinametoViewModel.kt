package br.com.reconhecimento.ui.treinamento

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
import kotlin.math.exp
import kotlin.math.pow
import kotlin.random.Random

@HiltViewModel
class ReconhecimentoLetrasViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val quantidadeVetoresTreinamento = 100
    private val quantidadeNeuroniosEscondidos = 10

    private val pesoV = pesosAletatorios(quantidadeNeuroniosEscondidos)
    private val pesoVBias = pesosAletatorios(quantidadeNeuroniosEscondidos)
    private val deltaPesoV = pesoPadrao
    private val deltaPesoVBias = pesoPadrao

    private val pesoW = pesosAletatorios(quantidadeNeuroniosEscondidos)
    private val pesoWBias = ((-1.0).pow(Random.nextDouble(10.0)) * Random.nextDouble()) / 3
    private val deltaPesoW = pesoPadrao
    private val deltaPesoWBias = 0.000

    private val vetorEntradaX = vetorEntradaPadrao
    private val vetorSaidaDesejadaY = vetorEntradaPadrao

    private val zIn = pesoPadrao
    private val z = pesoPadrao

    val taxaAprendizagem = 0.05
    val numeroDeCiclosDesejados = 35000

    fun treinar() {
        var ciclos = 0
        var erroParcial = 0.0

        var t: Double

        var yIn: Double
        var y: Double

        var deltinha: Double
        var deltinhaIn: Double
        var momento: Double = 0.005

        while (ciclos < numeroDeCiclosDesejados) {
            ciclos += 1
            erroParcial = 0.0

            (0..quantidadeVetoresTreinamento)

            for (vetorAtual in 0 until quantidadeVetoresTreinamento) {
                yIn = 0.0

                for (j in 0 until quantidadeNeuroniosEscondidos) {
                    zIn[j] = 0.0
                    zIn[j] = vetorEntradaX[vetorAtual] * pesoV[j]
                    zIn[j] += pesoVBias[j]

                    z[j] = zIn[j].funcaoDeAtivacao()
                    yIn += (z[j] * pesoW[j])
                }

                yIn += pesoWBias

                y = yIn.funcaoDeAtivacao()

                t = vetorSaidaDesejadaY[vetorAtual]

                deltinha = (t - y) * yIn.derivadaDaFuncaoDeAtivacao()

                erroParcial += (0.5) * ((t - y) * (t - y))

                for (i in 0 until quantidadeNeuroniosEscondidos){
                    deltaPesoW[i] = (taxaAprendizagem * deltinha * z[i]) * momento
                }

                deltaPesoWBias =
            }
        }

    }

}


fun pesosAletatorios(quantidadeNeuroniosEscondidos: Int): MutableList<Double> {
    val list = mutableListOf<Double>()
    repeat(quantidadeNeuroniosEscondidos) {
        val numberRamdon = ((-1.0).pow(Random.nextDouble(10.0)) * Random.nextDouble()) / 3
        list.add(it, numberRamdon)
    }

    return list
}

fun Double.derivadaDaFuncaoDeAtivacao(): Double {
    val funcaoX = (2.0 / (1.0 + Math.exp(-1.0 * this)) - 1.0)
    return (0.5 * ((1.0 + funcaoX) * (1.0 - funcaoX)))
}

fun Double.funcaoDeAtivacao(): Double {
    val exponencial = exp(-1.0 * this)
    return ((2.0 / (1.0 + exponencial)) - 1.0)
}

val pesoPadrao = mutableListOf<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
val vetorEntradaPadrao = mutableListOf<Double>().apply {
    repeat(100) {
        this.add(it, 0.0)
    }
}

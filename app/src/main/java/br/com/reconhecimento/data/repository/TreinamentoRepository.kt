package br.com.reconhecimento.data.repository

import android.util.Log
import br.com.reconhecimento.data.model.Erro
import br.com.reconhecimento.data.model.TreinoRealizado
import br.com.reconhecimento.data.model.Treinos
import br.com.reconhecimento.database.dao.ErroDao
import br.com.reconhecimento.database.dao.FuncaoAtivacaoDao
import br.com.reconhecimento.database.dao.TreinosDao
import br.com.reconhecimento.database.entity.ErroEntity
import br.com.reconhecimento.database.entity.FuncaoAtivacaoEntity
import br.com.reconhecimento.database.entity.TreinosEntity
import br.com.reconhecimento.database.entity.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

@Singleton
class TreinamentoRepository @Inject constructor(
    private val treinosDao: TreinosDao,
    private val erroDao: ErroDao,
    private val funcaoAtivacaoDao: FuncaoAtivacaoDao,
) {

    suspend fun observeUltimoTreino(): Flow<List<Treinos>> {
        return treinosDao.ultimoTreinoStream().map {
            treinosDao.getByIdTreino(it ?: 0).map(TreinosEntity::toModel)
        }
    }

    suspend fun observeErros(): Flow<List<Erro>> {
        return treinosDao.ultimoTreinoStream().map {
            val result = erroDao.getByIdTreino(it ?: 0)
            if (result.size > 10) {
                result.chunked(result.size.div(10)).map { list ->
                    val media = list.map { erro -> erro.erro }.average()
                    Erro(
                        erro = media,
                        idTreino = list.first().idTreino
                    )
                }
            } else {
                result.map { it.toModel() }
            }
        }
    }


    suspend fun treinar(taxaAprendizagem: Double, numeroDeCiclosDesejados: Int, momento: Double) {

        val quantidadeVetoresTreinamento = 100
        val quantidadeNeuroniosEscondidos = 10

        val pesoV = pesosAletatorios(quantidadeNeuroniosEscondidos)
        val pesoVBias = pesosAletatorios(quantidadeNeuroniosEscondidos)
        val deltaPesoV = mutableListOf<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        val deltaPesoVBias = mutableListOf<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        val pesoW = pesosAletatorios(quantidadeNeuroniosEscondidos)
        var pesoWBias = ((-1.0).pow(Random.nextInt(10)) * Random.nextDouble()) / 3
        val registroPesoWBias =
            mutableListOf<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        val deltaPesoW = mutableListOf<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        var deltaPesoWBias = 0.000

        val vetorEntradaX = mutableListOf<Double>().apply {
            repeat(100) {
                this.add(it, 0.0)
            }
        }
        val vetorSaidaDesejadaY = mutableListOf<Double>().apply {
            repeat(100) {
                this.add(it, 0.0)
            }
        }

        val zIn = mutableListOf<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        val z = mutableListOf<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        var ciclos = 0
        var erroParcial = 0.0
        var erros = mutableListOf<Double>()

        var t: Double

        var yIn: Double
        var y: Double

        var deltinha: Double = 0.0
        var deltinhaIn: Double = 0.0

        preencherEntradas(
            { index, valor -> vetorEntradaX[index] = valor },
            { index, valor -> vetorSaidaDesejadaY[index] = valor },
            quantidadeVetoresTreinamento
        )

        while (ciclos < numeroDeCiclosDesejados) {
            ciclos += 1

            erroParcial = 0.0
            Log.d("teste", erroParcial.toString())

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

                for (i in 0 until quantidadeNeuroniosEscondidos) {
                    deltaPesoW[i] = (taxaAprendizagem * deltinha * z[i]) * momento
                }

                deltaPesoWBias = (taxaAprendizagem * deltinha) * momento

                for (i in 0 until quantidadeNeuroniosEscondidos) {
                    deltinhaIn = deltinha * pesoW[i]
                    deltinha = deltinhaIn * zIn[i].derivadaDaFuncaoDeAtivacao()

                    deltaPesoV[i] =
                        taxaAprendizagem * deltinha * vetorEntradaX[vetorAtual] * momento
                    deltaPesoVBias[i] = (taxaAprendizagem * deltinha) * momento

                    pesoV[i] += deltaPesoV[i]
                    pesoVBias[i] += deltaPesoVBias[i]


                }

                for (i in 0 until quantidadeNeuroniosEscondidos) {
                    pesoW[i] += deltaPesoW[i]
                }

                pesoWBias += deltaPesoWBias

            }
            erros.add(erroParcial)
        }
        var x1 = 0.0

        var netEscondida = 0.0
        var outEscondida = 0.0
        var netSaida = 0.0
        var outSaida = 0.0

        val ultimoTreino = treinosDao.ultimoTreino() ?: 0
        val funcaoAtivacao = mutableListOf<FuncaoAtivacaoEntity>()
        for (j in 0 until quantidadeVetoresTreinamento) {
            netSaida = 0.0
            for (i in 0 until quantidadeNeuroniosEscondidos) {
                netEscondida = (vetorEntradaX[j] * pesoV[i])
                netEscondida += pesoVBias[i]

                outEscondida = netEscondida.funcaoDeAtivacao()

                netSaida += (outEscondida + pesoW[i])
            }
            netSaida += pesoWBias

            outSaida = netSaida.funcaoDeAtivacao()

            funcaoAtivacao.add(
                FuncaoAtivacaoEntity(
                    0,
                    ultimoTreino,
                    vetorEntradaX[j],
                    outSaida
                )
            )
        }

        val idTreino = ultimoTreino + 1
        val listaTreinos = pesoVBias.mapIndexed { index, biasV ->
            TreinosEntity(
                0,
                idTreino = idTreino,
                neuronio = (index + 1).toLong(),
                biasV = biasV,
                pesoV = pesoV[index],
                biasW = registroPesoWBias[index],
                pesoW = pesoW[index],
            )
        }
        treinosDao.insertAll(listaTreinos)
        erroDao.insertAll(erros.map {
            ErroEntity(
                0,
                idTreino = idTreino,
                erro = it
            )
        })
        funcaoAtivacaoDao.insertAll(funcaoAtivacao)
    }


    fun getRelatorios(): List<TreinoRealizado> {
        return treinosDao.getAll().groupBy { it.idTreino }.map {
            val lastFuncao = funcaoAtivacaoDao.getUltimaDoTreino(it.key)
            val diferenca = lastFuncao.saida - lastFuncao.vetor

            Pair(diferenca, it.value)
        }.sortedBy { it.first }.map {
            TreinoRealizado(
                idTreino = it.second.firstOrNull()?.idTreino ?: 0,
                neuronios = it.second.map { it.toModel() }
            )
        }
    }
}


fun pesosAletatorios(quantidadeNeuroniosEscondidos: Int): MutableList<Double> {
    val list = mutableListOf<Double>()
    repeat(quantidadeNeuroniosEscondidos) {
        val numberRamdon = ((-1.0).pow(Random.nextInt(10)) * Random.nextDouble()) / 3
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

fun preencherEntradas(
    vetorEntradasX: (Int, Double) -> Unit,
    vetorEntradasDesejadasY: (Int, Double) -> Unit,
    quantidadeVetoresTreinamento: Int
) {

    val pi = 3.1415
    var posicaoAtual = 0
    var y = 0.0

    var i = 0.0
    while (i < (2 * pi)) {
        i += ((2 * pi) / quantidadeVetoresTreinamento)

        y = sin(i) * sin(2 * i)

        vetorEntradasX(posicaoAtual, i)
        vetorEntradasDesejadasY(posicaoAtual, y)
        posicaoAtual += 1

        if (posicaoAtual >= quantidadeVetoresTreinamento) break
    }

}
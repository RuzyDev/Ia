package br.com.reconhecimento.data.domain

import br.com.reconhecimento.data.repository.TreinamentoRepository
import br.com.reconhecimento.di.AppCoroutineDispatchers
import br.com.reconhecimento.util.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Treinar @Inject constructor(
    private val treinamentoRepository: TreinamentoRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<Treinar.Params>() {

    class Params(
        val taxaAprendizagem: Double,
        val numeroDeCiclosDesejados: Int,
        val momento: Double
    )

    override suspend fun doWork(params: Params) {
        return withContext(dispatchers.io) {
            treinamentoRepository.treinar(
                taxaAprendizagem = params.taxaAprendizagem,
                numeroDeCiclosDesejados = params.numeroDeCiclosDesejados,
                momento = params.momento,
            )
        }
    }
}
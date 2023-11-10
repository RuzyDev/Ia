package br.com.reconhecimento.data.domain

import br.com.reconhecimento.data.model.TreinoRealizado
import br.com.reconhecimento.data.model.Treinos
import br.com.reconhecimento.data.repository.TreinamentoRepository
import br.com.reconhecimento.di.AppCoroutineDispatchers
import br.com.reconhecimento.util.Interactor
import br.com.reconhecimento.util.ResultInteractor
import br.com.reconhecimento.util.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRelatorio @Inject constructor(
    private val treinamentoRepository: TreinamentoRepository,
    private val dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GetRelatorio.Params, List<TreinoRealizado>>() {

    class Params()

    override suspend fun doWork(params: Params): List<TreinoRealizado> {
        return withContext(dispatchers.io) {
            treinamentoRepository.getRelatorios()
        }
    }
}
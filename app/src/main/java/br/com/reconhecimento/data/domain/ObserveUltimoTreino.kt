package br.com.reconhecimento.data.domain

import br.com.reconhecimento.data.model.Treinos
import br.com.reconhecimento.data.repository.TreinamentoRepository
import br.com.reconhecimento.di.AppCoroutineDispatchers
import br.com.reconhecimento.util.Interactor
import br.com.reconhecimento.util.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveUltimoTreino @Inject constructor(
    private val treinamentoRepository: TreinamentoRepository,
    private val dispatchers: AppCoroutineDispatchers
) : SubjectInteractor<ObserveUltimoTreino.Params, List<Treinos>>() {

    class Params()

    override suspend fun createObservable(params: Params): Flow<List<Treinos>> {
        return withContext(dispatchers.io) {
            treinamentoRepository.observeUltimoTreino()
        }
    }
}
package br.com.arcom.signpad.data.domain

import br.com.arcom.signpad.data.model.LgpdVisitante
import br.com.arcom.signpad.data.repository.SignPadRepository
import br.com.arcom.signpad.di.AppCoroutineDispatchers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FindVisitantes @Inject constructor(
    private val signPadRepository: SignPadRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<FindVisitantes.Params, List<LgpdVisitante>>() {

    data class Params(
        val search: String
    )

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun doWork(params: Params): List<LgpdVisitante> {
        return withContext(dispatchers.io) {
            signPadRepository.findVisitantes(params.search)
        }
    }
}
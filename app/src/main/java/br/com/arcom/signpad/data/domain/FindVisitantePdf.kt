package br.com.arcom.signpad.data.domain

import br.com.arcom.signpad.data.model.LgpdVisitante
import br.com.arcom.signpad.data.repository.SignPadRepository
import br.com.arcom.signpad.di.AppCoroutineDispatchers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FindVisitantePdf @Inject constructor(
    private val signPadRepository: SignPadRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<FindVisitantePdf.Params, String?>() {

    data class Params(
        val cpf: Long
    )

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun doWork(params: Params): String? {
        return withContext(dispatchers.io) {
            signPadRepository.findVisitantePdf(params.cpf)
        }
    }
}
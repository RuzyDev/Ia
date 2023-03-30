package br.com.arcom.signpad.data.domain

import br.com.arcom.signpad.data.repository.SignPadRepository
import br.com.arcom.signpad.di.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendVisitante @Inject constructor(
    private val signPadRepository: SignPadRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<SendVisitante.Params>() {

    data class Params(
        val nome: String,
        val cpf: Long,
        val filePdf: String
    )

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            signPadRepository.enviarVisitante(
                params.nome,
                params.cpf,
                params.filePdf,
            )
        }
    }
}
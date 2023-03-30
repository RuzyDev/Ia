package br.com.arcom.signpad.data.domain

import br.com.arcom.signpad.data.repository.SignPadRepository
import br.com.arcom.signpad.di.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendEmail @Inject constructor(
    private val signPadRepository: SignPadRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<SendEmail.Params>() {

    data class Params(
        val email: String,
        val cpf: Long
    )

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            signPadRepository.enviarPdfPorEmail(
                params.email,
                params.cpf,
            )
        }
    }
}
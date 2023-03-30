package br.com.arcom.signpad.data.repository

import br.com.arcom.signpad.network.bodyOrThrow
import br.com.arcom.signpad.network.model.NetworkDadosLgpdVisitante
import br.com.arcom.signpad.network.services.SignpadService
import br.com.arcom.signpad.network.withRetry
import javax.inject.Inject

class SignPadDataSource @Inject constructor(
    val signpadService: SignpadService
) {

    suspend fun salvarDadosLgpdVisitante(dados: NetworkDadosLgpdVisitante) = withRetry {
        signpadService.salvarDadosLgpdVisitante(dados).bodyOrThrow()
    }

    suspend fun findVisitantesCpf(cpf: Long) = withRetry {
        signpadService.buscarLpgdVisitante("cpf", cpf).bodyOrThrow()
    }

    suspend fun findVisitantesNome(nome: String) = withRetry {
        signpadService.buscarLpgdVisitante("nome", nome).bodyOrThrow()
    }

    suspend fun enviarPdfPorEmail(email: String, cpf: Long) = withRetry {
        signpadService.enviarPdfPorEmail(email =email, cpf =cpf).bodyOrThrow()
    }

}
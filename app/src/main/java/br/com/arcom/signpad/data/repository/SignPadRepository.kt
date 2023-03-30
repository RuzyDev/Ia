package br.com.arcom.signpad.data.repository

import br.com.arcom.signpad.data.model.LgpdVisitante
import br.com.arcom.signpad.database.dao.LgpdVisitanteDao
import br.com.arcom.signpad.database.entity.LgpdVisitanteEntity
import br.com.arcom.signpad.network.model.NetworkDadosLgpdVisitante
import br.com.arcom.signpad.network.model.asModel
import br.com.arcom.signpad.util.asNumber
import br.com.arcom.signpad.util.encodeFileToBase64Binary
import br.com.arcom.signpad.util.formatData
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignPadRepository @Inject constructor(
    private val signPadDataSource: SignPadDataSource,
    private val lgpdVisitanteDao: LgpdVisitanteDao
) {
    suspend fun enviarVisitante(
        nome: String,
        cpf: Long,
        filePdf: String
    ) {
        val base64 = encodeFileToBase64Binary(File(filePdf))
        val now = LocalDateTime.now().formatData("dd-MM-yyyy HH:mm:ss")
        val visitantesNaoEnviados = lgpdVisitanteDao.getAll()

        try {
            visitantesNaoEnviados.forEach {
                signPadDataSource.salvarDadosLgpdVisitante(
                    NetworkDadosLgpdVisitante(
                        nome = it.nome,
                        cpf = it.cpf,
                        dataAss = it.dataAss,
                        filePart = it.pdfBase64,
                    )
                )
                lgpdVisitanteDao.deleteEntity(it)
            }
            signPadDataSource.salvarDadosLgpdVisitante(
                NetworkDadosLgpdVisitante(
                    nome = nome,
                    cpf = cpf,
                    dataAss = now,
                    filePart = base64,
                )
            )
        }catch (e: Exception){
            lgpdVisitanteDao.insert(
                LgpdVisitanteEntity(
                    id = 0,
                    nome = nome,
                    cpf = cpf,
                    dataAss = now,
                    pdfBase64 = base64,
                )
            )
        }
    }

    suspend fun findVisitantes(
        search: String
    ): List<LgpdVisitante> {
        return (if (search.asNumber()) {
            val visitante = signPadDataSource.findVisitantesCpf(search.toLong())
            if (visitante != null) listOf(visitante) else emptyList()
        } else {
            signPadDataSource.findVisitantesNome(search)
        })?.map { it.asModel() } ?: emptyList()
    }

    suspend fun enviarPdfPorEmail(email: String, cpf: Long) {
        signPadDataSource.enviarPdfPorEmail(
            cpf = cpf,
            email = email
        )
    }

}
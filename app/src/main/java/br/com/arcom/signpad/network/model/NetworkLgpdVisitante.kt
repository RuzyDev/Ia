package br.com.arcom.signpad.network.model

import br.com.arcom.signpad.data.model.LgpdVisitante
import br.com.arcom.signpad.util.formatDate
import br.com.arcom.signpad.util.formatDateTime
import br.com.arcom.signpad.util.formatServer
import kotlinx.serialization.Serializable

@Serializable
data class NetworkLgpdVisitante(
    val id: Long,
    val nomeVisitante: String,
    val dataAssinatura: String,
    val arquivoLgpdVisitante: String,
)

fun NetworkLgpdVisitante.asModel() = LgpdVisitante(
    id = id,
    nomeVisitante = nomeVisitante,
    dataAssinatura = dataAssinatura.formatDateTime("yyyy-MM-dd'T'HH:mm:ss"),
    arquivoLgpdVisitante = arquivoLgpdVisitante,
)
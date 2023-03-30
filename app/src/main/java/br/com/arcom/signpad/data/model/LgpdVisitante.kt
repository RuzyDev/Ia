package br.com.arcom.signpad.data.model

import java.time.LocalDateTime

data class LgpdVisitante(
    val id: Long,
    val nomeVisitante: String,
    val dataAssinatura: LocalDateTime,
    val arquivoLgpdVisitante: String,
)
package br.com.arcom.signpad.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkDadosLgpdVisitante (
    val nome : String,
    val cpf: Long,
    val dataAss: String,
    val filePart: String,
)
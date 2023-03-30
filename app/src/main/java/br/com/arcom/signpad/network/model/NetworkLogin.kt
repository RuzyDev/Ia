package br.com.arcom.signpad.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkLogin (
    val idUsuario : Long,
    val senha: String
)
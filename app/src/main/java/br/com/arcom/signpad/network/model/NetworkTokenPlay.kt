package br.com.arcom.signpad.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTokenPlay (
    val token: String? = null,
    val expira: String? = null,
)




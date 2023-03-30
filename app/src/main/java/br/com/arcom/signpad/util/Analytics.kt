package br.com.arcom.signpad.util

interface Analytics {
    fun trackScreenView(
        label: String,
        route: String?,
        arguments: Any? = null,
    )
}
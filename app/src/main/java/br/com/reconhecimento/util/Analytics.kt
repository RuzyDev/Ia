package br.com.reconhecimento.util

interface Analytics {
    fun trackScreenView(
        label: String,
        route: String?,
        arguments: Any? = null,
    )
}
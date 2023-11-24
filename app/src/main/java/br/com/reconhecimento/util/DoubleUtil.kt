package br.com.reconhecimento.util

import java.math.BigDecimal
import java.text.DecimalFormat

fun Double.formatCasasDecimais(casas: Int = 1): String {
    var fomart = if (casas > 0) "#." else "#.#"
    val casasString = repeat(casas) { fomart += "#" }
    val formato = DecimalFormat(fomart)
    return formato.format(this)
}

fun BigDecimal.formatCasasDecimais(casas: Int = 1): String {
    var fomart = if (casas > 0) "#." else "#.#"
    val casasString = repeat(casas) { fomart += "#" }
    val formato = DecimalFormat(fomart)
    return formato.format(this)
}
package br.com.reconhecimento.data.model

enum class Letras(val fontes: List<Fonte>) {
    A(fontesA.map { Fonte(it) }),
    B(fontesB.map { Fonte(it) }),
    C(fontesC.map { Fonte(it) }),
}

data class Fonte(
    val values: MutableList<MutableList<Int>>
) {
    fun getValueComBias(): List<Int> {
        return values.flatten().toMutableList().apply {
            this.add(1)
        }
    }
}
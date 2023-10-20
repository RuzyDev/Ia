package br.com.reconhecimento.data.model

enum class Letras(val index: Int,val fontes: List<Fonte>) {
    A(0,fontesA.map { Fonte(it) }),
    B(1,fontesB.map { Fonte(it) }),
    C(2,fontesC.map { Fonte(it) }),
    D(3,fontesD.map { Fonte(it) }),
    E(4,fontesE.map { Fonte(it) }),
    J(5,fontesJ.map { Fonte(it) }),
    K(6,fontesK.map { Fonte(it) });

    companion object{
        fun getByIndex(index: Int) = values().firstOrNull { it.index == index }
    }
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
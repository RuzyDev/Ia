package br.com.reconhecimento.data.model

val defaultPeso = mutableListOf<MutableList<Double>>().apply {
    repeat(7) {
        val newList = mutableListOf<Double>()
        repeat(64) {
            newList.add(0.0)
        }
        add(newList)
    }
}

package br.com.reconhecimento.data.model

import android.util.Log

val saidasDesejadas = mutableListOf<List<Int>>().apply {
    var inicio = 0
    for (letras in 0..6) {
        add(mutableListOf<Int>().apply {
            val newList = this
            for (i in 0..20) {
                if (i < (inicio + 3) && i >= inicio){
                    newList.add(1)
                }else{
                    newList.add(-1)
                }
            }
            Log.d("saida desejada", newList.joinToString())
        })
        inicio += 3
    }
}
package br.com.reconhecimento.data.model

private val fonteK1 = mutableListOf(
    mutableListOf(1,1,-1,-1,-1,1,1),
    mutableListOf(-1,1,-1,-1,1,-1,-1),
    mutableListOf(-1,1,-1,1,-1,-1,-1),
    mutableListOf(-1,1,1,-1,-1,-1,-1),
    mutableListOf(-1,1,1,-1,-1,-1,-1),
    mutableListOf(-1,1,-1,1,-1,-1,-1),
    mutableListOf(-1,1,-1,-1,1,-1,-1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(1,1,-1,-1,-1,1,1),
)

private val fonteK2 = mutableListOf(
    mutableListOf(1,-1,-1,-1,-1,1,-1),
    mutableListOf(1,-1,-1,-1,1,-1,-1),
    mutableListOf(1,-1,-1,1,-1,-1,-1),
    mutableListOf(1,-1,1,-1,-1,-1,-1),
    mutableListOf(1,1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,1,-1),
)

private val fonteK3 = mutableListOf(
    mutableListOf(1,1,-1,-1,-1,1,1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(-1,1,-1,-1,1,-1,-1),
    mutableListOf(-1,1,-1,1,-1,-1,-1),
    mutableListOf(-1,1,1,-1,-1,-1,-1),
    mutableListOf(-1,1,-1,1,-1,-1,-1),
    mutableListOf(-1,1,-1,-1,1,-1,-1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(1,1,-1,-1,-1,1,1),
)

val fontesK = listOf(fonteK1, fonteK2, fonteK3)
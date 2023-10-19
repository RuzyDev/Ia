package br.com.reconhecimento.data.model

private val fonteA1 = mutableListOf(
    mutableListOf(-1,-1,1,1,-1,-1,-1),
    mutableListOf(-1,-1,-1,1,-1,-1,-1),
    mutableListOf(-1,-1,-1,1,-1,-1,-1),
    mutableListOf(-1,-1,1,-1,1,-1,-1),
    mutableListOf(-1,-1,1,-1,1,-1,-1),
    mutableListOf(-1,1,1,1,1,1,-1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(1,1,1,-1,1,1,1),
)

private val fonteA2 = mutableListOf(
    mutableListOf(-1,-1,-1,1,-1,-1,-1),
    mutableListOf(-1,-1,-1,1,-1,-1,-1),
    mutableListOf(-1,-1,-1,1,-1,-1,-1),
    mutableListOf(-1,-1,1,-1,1,-1,-1),
    mutableListOf(-1,-1,1,-1,1,-1,-1),
    mutableListOf(-1,1,1,1,1,1,-1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
)

private val fonteA3 = mutableListOf(
    mutableListOf(-1,-1,-1,1,-1,-1,-1),
    mutableListOf(-1,-1,-1,1,-1,-1,-1),
    mutableListOf(-1,-1,1,-1,1,-1,-1),
    mutableListOf(-1,-1,1,-1,1,-1,-1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(-1,1,1,1,1,1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,1),
    mutableListOf(1,-1,-1,-1,-1,-1,1),
    mutableListOf(1,1,-1,-1,-1,1,1),
)

val fontesA = listOf(fonteA1, fonteA2, fonteA3)
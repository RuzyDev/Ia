package br.com.reconhecimento.data.model

private val fonteC1 = mutableListOf(
    mutableListOf(-1,-1,1,1,1,1,1),
    mutableListOf(-1,1,-1,-1,-1,-1,1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(-1,1,-1,-1,-1,-1,1),
    mutableListOf(-1,-1,1,1,1,1,-1),
)

private val fonteC2 = mutableListOf(
    mutableListOf(-1,-1,1,1,1,-1,-1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(-1,-1,1,1,1,-1,-1),
)

private val fonteC3 = mutableListOf(
    mutableListOf(-1,-1,1,1,1,-1,1),
    mutableListOf(-1,1,-1,-1,-1,1,1),
    mutableListOf(1,-1,-1,-1,-1,-1,1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,1),
    mutableListOf(-1,1,-1,-1,-1,1,-1),
    mutableListOf(-1,-1,1,1,1,-1,-1),
)

val fontesC = listOf(fonteC1, fonteC2, fonteC3)
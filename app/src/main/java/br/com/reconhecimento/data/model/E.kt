package br.com.reconhecimento.data.model

private val fonteE1 = mutableListOf(
    mutableListOf(1,1,1,1,1,1,1),
    mutableListOf(-1,1,-1,-1,-1,-1,1),
    mutableListOf(-1,1,-1,-1,-1,-1,-1),
    mutableListOf(-1,1,-1,1,-1,-1,-1),
    mutableListOf(-1,1,1,1,-1,-1,-1),
    mutableListOf(-1,1,-1,1,-1,-1,-1),
    mutableListOf(-1,1,-1,-1,-1,-1,-1),
    mutableListOf(-1,1,-1,-1,-1,-1,1),
    mutableListOf(1,1,1,1,1,1,1),
)

private val fonteE2 = mutableListOf(
    mutableListOf(1,1,1,1,1,1,1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,1,1,1,1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,-1,-1,-1,-1,-1,-1),
    mutableListOf(1,1,1,1,1,1,1),
)

private val fonteE3 = mutableListOf(
    mutableListOf(1,1,1,1,1,1,1),
    mutableListOf(-1,1,-1,-1,-1,-1,1),
    mutableListOf(-1,1,-1,-1,1,-1,-1),
    mutableListOf(-1,1,1,1,1,-1,-1),
    mutableListOf(-1,1,-1,-1,1,-1,-1),
    mutableListOf(-1,1,-1,-1,-1,-1,-1),
    mutableListOf(-1,1,-1,-1,-1,-1,-1),
    mutableListOf(-1,1,-1,-1,-1,-1,1),
    mutableListOf(1,1,1,1,1,1,1),
)

val fontesE = listOf(fonteE1, fonteE2, fonteE3)
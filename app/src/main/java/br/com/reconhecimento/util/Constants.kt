package br.com.reconhecimento.util

const val DATABASE_NAME = "signPad-db"
val LOCALDATE_FORMATS = listOf("dd/MM/yyyy", "yyyy/MM/dd", "yy/MM/dd", "yyyy-MM-dd")
val LOCALDATETIME_FORMATS = listOf(
    "dd/MM/yyyy HH:mm:ss",
    "yyyy/MM/dd HH:mm:ss",
    "yyyy-MM-dd HH:mm:ss",
    "yyyy-MM-dd HH:mm:ss.SSS",
    "yyyy-MM-dd HH:mm:ss.SS",
    "yyyy-MM-dd HH:mm:ss.S",
    "dd-MM-yyyy HH:mm:ss",
    "dd-MM-yyyy HH:mm",
    "dd/MM/yyyy HH:mm",
    "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS",
    "yyyy-MM-dd'T'HH:mm:ss"
)
const val LOCALDATE_PADRAO = "yyyy-MM-dd"
const val LOCALDATETIME_PADRAO = "yyyy-MM-dd HH:mm:ss"
const val INFO_Reconhecimento = """    Este é um aplicativo de gerenciamento de suas viagens pela Arcom, ele não apenas registra sua chegada e partida de um destino, como também ajuda a calcular a distância percorrida entre os destinos e o custo total gasto com gasolina.

    Com ele, basta tirar uma foto do odômetro do seu carro e informar a quantidade de quilômetros registrados no odômetro ao chegar e partir de um destino. O aplicativo então calcula a distância percorrida e o custo total gasto com gasolina.

    Além disso, este aplicativo também permitirá que você seja reembolsado destes custos, tornando-o uma ferramenta indispensável para o registro de suas viagens."""
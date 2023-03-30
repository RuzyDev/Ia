package br.com.arcom.signpad.network.services

import br.com.arcom.signpad.network.model.NetworkDadosLgpdVisitante
import br.com.arcom.signpad.network.model.NetworkLogin
import br.com.arcom.signpad.network.model.NetworkTokenPlay
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface TokenPlayService {

    @POST("api/seguranca/v2/login")
    fun buscarTokenPlay(
        @Body login : NetworkLogin
    ): Call<String>
}
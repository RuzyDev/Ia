package br.com.arcom.signpad.network.services

import br.com.arcom.signpad.data.model.LgpdVisitante
import br.com.arcom.signpad.network.model.NetworkDadosLgpdVisitante
import br.com.arcom.signpad.network.model.NetworkLgpdVisitante
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface SignpadService {

    @POST("api/admin/v1/lgpd-visitante")
    suspend fun salvarDadosLgpdVisitante(
        @Body dados: NetworkDadosLgpdVisitante
    ): Response<Void?>

    @GET("/api/admin/v1/lgpd-visitante")
    suspend fun buscarLpgdVisitante(
        @Query("modoPesquisa") modoPesquisa: String?,
        @Query("nome") nome: String?
    ): Response<List<NetworkLgpdVisitante>?>

    @GET("/api/admin/v1/lgpd-visitante")
    suspend fun buscarLpgdVisitante(
        @Query("modoPesquisa") modoPesquisa: String?,
        @Query("cpf") cpf: Long?
    ): Response<NetworkLgpdVisitante?>

    @POST("/api/admin/v1/lgpd-visitante-enviar-email")
    suspend fun enviarPdfPorEmail(
        @Query("email") email: String?,
        @Query("cpf") cpf: Long?
    ): Response<Void?>

}

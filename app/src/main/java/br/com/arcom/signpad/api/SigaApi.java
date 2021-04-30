package br.com.arcom.signpad.api;

import java.util.List;

import br.com.arcom.signpad.data.LgpdVisitante;
import br.com.arcom.signpad.models.LgpdVisitanteRequest;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface SigaApi {

    @POST("api/seguranca/v2/login")
    Call<String> buscarToken(@Body LgpdVisitanteRequest lgpdVisitanteRequest);

    @Multipart
    @POST("api/admin/v1/lgpd-visitante")
    Call<Void> salvarDadosVisitante(
            @Header("Authorization") String token,
            @Query("nomeLgpdVisitante") String nome,
            @Query("cpfLgpdVisitante") Long cpf,
            @Query("dataPreenchimento") String dataAss,
            @Part MultipartBody.Part filePart
    );

    @GET("/api/admin/v1/lgpd-visitante")
    Call<List<LgpdVisitante>> buscarLpgdVisitante(
            @Header("Authorization") String token,
            @Query("modoPesquisa") String modoPesquisa,
            @Query("nome") String nome
    );

    @GET("/api/admin/v1/lgpd-visitante")
    Call<LgpdVisitante> buscarLpgdVisitante(
            @Header("Authorization") String token,
            @Query("modoPesquisa") String modoPesquisa,
            @Query("cpf") Long cpf
    );

}

package br.com.arcom.signpad.api;

import br.com.arcom.signpad.models.UsuarioRequest;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface SigaApi {

    @POST("api/seguranca/v2/login")
    Call<String> buscarToken(@Body UsuarioRequest usuarioRequest);

    @Multipart
    @POST("api/admin/v1/dados-lgpd-visitante")
    Call<Void> salvarDados(
            @Header("Authorization") String token,
            @Query("nomeLgpdVisitante") String nome,
            @Query("cpfLgpdVisitante") Long cpf,
            @Query("dataPreenchimento") String dataPreen,
            @Part MultipartBody.Part filePart);

}

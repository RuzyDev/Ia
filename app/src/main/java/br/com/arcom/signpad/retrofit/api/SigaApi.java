package br.com.arcom.signpad.retrofit.api;

import br.com.arcom.signpad.model.UsuarioRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SigaApi {

    @POST("api/seguranca/v2/login")
    Call<String> buscarToken(@Body UsuarioRequest usuarioRequest);

}

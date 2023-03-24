package br.com.arcom.signpad.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import br.com.arcom.signpad.utilities.Constantes;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SigaService {

    // url do servidor
    private final String baseUrl = !Constantes.DEV ? "http://siga.arcom.com.br/" : "http://172.19.1.98:9000/";
    private final SigaApi api;

    public SigaService() {
        Retrofit retrofit = this.criaRetrofit();
        this.api = retrofit.create(SigaApi.class);
    }

    public final String getBaseUrl() {
        return this.baseUrl;
    }

    public final SigaApi getApi() {
        return this.api;
    }

    public final Retrofit criaRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        Gson gson = (new GsonBuilder().setLenient()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        return (new retrofit2.Retrofit.Builder())
                .baseUrl(this.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();
    }
}

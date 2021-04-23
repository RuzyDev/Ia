package br.com.arcom.signpad.retrofit.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.arcom.signpad.retrofit.api.SigaApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SigaService {

    // url do servidor
    private final String baseUrl = "http://siga.arcom.com.br/";
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
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Gson gson = (new GsonBuilder().setLenient()).setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        return (new retrofit2.Retrofit.Builder())
                .baseUrl(this.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();
    }
}

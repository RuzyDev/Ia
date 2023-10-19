package br.com.reconhecimento.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ReconhecimentoInterceptor(val api: NetworkReconhecimentoApi) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val urlBuilder: HttpUrl.Builder = request.url.newBuilder()
        val builder: Request.Builder =
            request.newBuilder()
                .addHeader("Connection","close")
                .addHeader("Authorization", "Bearer ${api.getToken()}").url(urlBuilder.build())
        val response = chain.proceed(builder.build())
        return response
    }

}
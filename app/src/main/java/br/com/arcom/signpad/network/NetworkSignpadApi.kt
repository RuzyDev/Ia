package br.com.arcom.signpad.network

import android.content.Context
import android.content.SharedPreferences
import br.com.arcom.signpad.network.model.NetworkLogin
import br.com.arcom.signpad.network.services.*
import br.com.arcom.signpad.util.getSenhaDoDia
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.*
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class NetworkSignpadApi(
    val apiBaseUrl: String,
    val context: Context,
    val tokenPlayPrefs: SharedPreferences,
    val json: Json
) {

    private var retrofit: Retrofit? = null

    var repeatCallToken = 0

    private var tokenPlay: String? = null

    @OptIn(ExperimentalSerializationApi::class)
    protected fun retrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .client(
                    OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .authenticator(SignpadAuthenticator(this))
                        .addInterceptor(SignpadInterceptor(this))
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        })
                        .addNetworkInterceptor {
                            val originalResponse = it.proceed(it.request())
                            val request = it.request()
                            val progressCallback = request.tag(ProgressListener::class.java)
                            if (progressCallback != null) {
                                return@addNetworkInterceptor originalResponse.newBuilder()
                                    .body(
                                        ProgressResponseBody(
                                            originalResponse.body,
                                            progressCallback
                                        )
                                    )
                                    .build()
                            }
                            return@addNetworkInterceptor originalResponse
                        }.build()
                ).build()
        }
        return retrofit!!
    }

    fun atualizarTokenPlay(token: String) {
        tokenPlay = token
    }

    fun getTokenPlay(): String {
        return tokenPlay ?: "0"
    }

    fun clearToken() {
        tokenPlay = null
    }


    fun appTokenPlayService(): TokenPlayService {
        return retrofit().create(TokenPlayService::class.java)
    }

    fun appSignpadService(): SignpadService {
        return retrofit().create(SignpadService::class.java)
    }


    fun updateTokenPlay(): String? {
        repeatCallToken++
        val tokenResponse = getUpdatedToken()
        if (tokenResponse != null) {
            repeatCallToken = 0
            tokenPlayPrefs.edit().putString("tokenPlay", tokenResponse).commit()
            tokenPlay = tokenResponse
        } else {
            throw IOException("Erro ao autenticar com o Arcom ID!")
        }

        return tokenPlay
    }

    private fun getUpdatedToken(): String? {
        val response =
            appTokenPlayService().buscarTokenPlay(NetworkLogin(43, getSenhaDoDia().toString()))
                .execute()
        if (response.code() == 401 || response.code() == 403) repeatCallToken++ else repeatCallToken =
            0
        return response.bodyOrThrow()
    }

    private class ProgressResponseBody internal constructor(
        private val responseBody: ResponseBody,
        private val progressListener: ProgressListener
    ) : ResponseBody() {

        private var bufferedSource: BufferedSource? = null
        override fun contentType(): MediaType? {
            return responseBody.contentType()
        }

        override fun contentLength(): Long {
            return responseBody.contentLength()
        }

        override fun source(): BufferedSource {
            if (bufferedSource == null) {
                bufferedSource = source(responseBody.source()).buffer()
            }
            return bufferedSource!!
        }

        private fun source(source: Source): Source {
            return object : ForwardingSource(source) {
                var totalBytesRead = 0L

                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    progressListener.update(
                        totalBytesRead,
                        responseBody.contentLength(),
                        bytesRead == -1L
                    )
                    return bytesRead
                }
            }
        }
    }

    interface ProgressListener {
        fun update(bytesRead: Long, contentLength: Long, done: Boolean)
    }
}
package br.com.reconhecimento.network

import android.content.Context
import android.content.SharedPreferences
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


class NetworkReconhecimentoApi(
    val apiBaseUrl: String,
    val context: Context,
    val tokenPrefs: SharedPreferences,
    val json: Json
) {

    private var retrofit: Retrofit? = null

    var repeatCallToken = 0

    private var tokenServer: String? = null

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
                        .authenticator(ReconhecimentoAuthenticator(this))
                        .addInterceptor(ReconhecimentoInterceptor(this))
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

    fun atualizarToken(token: String) {
        tokenServer = token
    }

    fun getToken(): String {
        return tokenServer ?: "0"
    }

    fun clearToken() {
        tokenServer = null
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
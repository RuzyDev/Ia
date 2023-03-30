package br.com.arcom.signpad.network

import okhttp3.*
import java.io.IOException


class SignpadAuthenticator(val api: NetworkSignpadApi) : Authenticator {

    @kotlin.jvm.Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request {
        if (responseCount(response) >= 2 || api.repeatCallToken > 2) {
            api.repeatCallToken = 0
            throw IOException("Erro ao autenticar")
        }
        api.clearToken()
        val tokenPlay = api.updateTokenPlay()
        return response.request.newBuilder()
            .header("Authorization", "Bearer $tokenPlay").header("Connection","close").build()

    }

    private fun responseCount(response: Response): Int {
        var result = 1
        while (response.priorResponse != null) {
            if(result++ > 2) break
        }
        return result
    }
}
package br.com.reconhecimento.network

import okhttp3.*
import java.io.IOException


class ReconhecimentoAuthenticator(val api: NetworkReconhecimentoApi) : Authenticator {

    @kotlin.jvm.Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request {
        if (responseCount(response) >= 2 || api.repeatCallToken > 2) {
            api.repeatCallToken = 0
            throw IOException("Erro ao autenticar")
        }
        api.clearToken()
        return response.request.newBuilder().header("Connection","close").build()

    }

    private fun responseCount(response: Response): Int {
        var result = 1
        while (response.priorResponse != null) {
            if(result++ > 2) break
        }
        return result
    }
}
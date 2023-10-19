package br.com.reconhecimento.di

import android.content.Context
import android.content.SharedPreferences
import br.com.reconhecimento.network.NetworkReconhecimentoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "http://siga.arcom.com.br"

@InstallIn(SingletonComponent::class)
@Module(includes = [ApiServiceModule::class])
object NetworkRetrofitRepModule {

    @Provides
    @Singleton
    fun provideNetworkSignpadApi(
        @ApplicationContext context: Context,
        @Named("token") tokenPrefs: SharedPreferences,
        json: Json
    ): NetworkReconhecimentoApi = NetworkReconhecimentoApi(BASE_URL, context, tokenPrefs, json)

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

}

@InstallIn(SingletonComponent::class)
@Module
object ApiServiceModule {}
package br.com.arcom.signpad.di

import android.content.Context
import android.content.SharedPreferences
import br.com.arcom.signpad.network.NetworkSignpadApi
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
        @Named("tokenPlay") tokenPlayPrefs: SharedPreferences,
        json: Json
    ): NetworkSignpadApi = NetworkSignpadApi(BASE_URL, context, tokenPlayPrefs, json)

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

}

@InstallIn(SingletonComponent::class)
@Module
object ApiServiceModule {

    @Provides
    fun provideTokenPlayService(networkRepApi: NetworkSignpadApi) = networkRepApi.appTokenPlayService()

    @Provides
    fun provideSignpadService(networkRepApi: NetworkSignpadApi) = networkRepApi.appSignpadService()

}
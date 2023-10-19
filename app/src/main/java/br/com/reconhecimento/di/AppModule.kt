package br.com.reconhecimento.di

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import br.com.reconhecimento.util.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideCoroutineDispatchers() = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Singleton
    @Provides
    @Named("token")
    fun provideTokenSharedPrefs(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("token", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)


    @Singleton
    @Provides
    fun provideDiretorios(
        @ApplicationContext context: Context
    ): Files {
        return Files(context)
    }

}

@InstallIn(SingletonComponent::class)
@Module
abstract class BaseModule {
    @Singleton
    @Binds
    internal abstract fun provideLogger(bind: AppLogger): Logger

}
package br.com.reconhecimento.di

import android.content.Context
import br.com.reconhecimento.database.dao.ErroDao
import br.com.reconhecimento.database.dao.FuncaoAtivacaoDao
import br.com.reconhecimento.database.dao.TreinosDao
import br.com.reconhecimento.database.db.AppDatabase
import br.com.reconhecimento.database.db.DatabaseTransactionRunner
import br.com.reconhecimento.database.db.RoomTransactionRunner
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideTreinosDao(appDatabase: AppDatabase): TreinosDao {
        return appDatabase.treinosDao()
    }

    @Provides
    fun provideErroDao(appDatabase: AppDatabase): ErroDao {
        return appDatabase.erroDao()
    }

    @Provides
    fun provideFuncaoAtivacaoDao(appDatabase: AppDatabase): FuncaoAtivacaoDao {
        return appDatabase.funcaoAtivacaoDao()
    }
}


@InstallIn(SingletonComponent::class)
@Module
abstract class DatabaseModuleBinds {

    @Singleton
    @Binds
    abstract fun provideDatabaseTransactionRunner(runner: RoomTransactionRunner): DatabaseTransactionRunner
}
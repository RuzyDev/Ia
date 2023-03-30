package br.com.arcom.signpad.di

import android.content.Context
import br.com.arcom.signpad.database.dao.LgpdVisitanteDao
import br.com.arcom.signpad.database.db.AppDatabase
import br.com.arcom.signpad.database.db.DatabaseTransactionRunner
import br.com.arcom.signpad.database.db.RoomTransactionRunner
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
    fun provideLgpdVisitanteDao(appDatabase: AppDatabase): LgpdVisitanteDao {
        return appDatabase.lgpdVisitanteDao()
    }

}


@InstallIn(SingletonComponent::class)
@Module
abstract class DatabaseModuleBinds {

    @Singleton
    @Binds
    abstract fun provideDatabaseTransactionRunner(runner: RoomTransactionRunner): DatabaseTransactionRunner
}
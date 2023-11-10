package br.com.reconhecimento.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.reconhecimento.data.model.FuncaoAtivacao
import br.com.reconhecimento.database.converter.AppTypeConverters
import br.com.reconhecimento.database.dao.ErroDao
import br.com.reconhecimento.database.dao.FuncaoAtivacaoDao
import br.com.reconhecimento.database.dao.TreinosDao
import br.com.reconhecimento.database.entity.ErroEntity
import br.com.reconhecimento.database.entity.FuncaoAtivacaoEntity
import br.com.reconhecimento.database.entity.TreinosEntity
import br.com.reconhecimento.util.DATABASE_NAME

@Database(
    entities = [TreinosEntity::class, ErroEntity::class, FuncaoAtivacaoEntity::class],
    version = 1, exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun treinosDao(): TreinosDao
    abstract fun erroDao(): ErroDao
    abstract fun funcaoAtivacaoDao(): FuncaoAtivacaoDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
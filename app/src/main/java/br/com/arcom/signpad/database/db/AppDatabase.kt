package br.com.arcom.signpad.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.arcom.signpad.database.converter.AppTypeConverters
import br.com.arcom.signpad.database.dao.LgpdVisitanteDao
import br.com.arcom.signpad.database.entity.LgpdVisitanteEntity
import br.com.arcom.signpad.util.DATABASE_NAME

@Database(
    entities = [
        LgpdVisitanteEntity::class
    ],
    version = 1, exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lgpdVisitanteDao(): LgpdVisitanteDao

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
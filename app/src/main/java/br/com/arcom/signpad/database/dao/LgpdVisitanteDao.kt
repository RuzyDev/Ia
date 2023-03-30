package br.com.arcom.signpad.database.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.arcom.signpad.database.entity.LgpdVisitanteEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class LgpdVisitanteDao : EntityDao<LgpdVisitanteEntity>() {

    @Query("SELECT * from lgpdVisitante")
    abstract fun getAllStream(): Flow<List<LgpdVisitanteEntity>>

    @Query("SELECT * from lgpdVisitante")
    abstract suspend fun getAll(): List<LgpdVisitanteEntity>
}
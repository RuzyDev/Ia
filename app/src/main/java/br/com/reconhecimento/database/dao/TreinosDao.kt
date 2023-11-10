package br.com.reconhecimento.database.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.reconhecimento.database.entity.TreinosEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TreinosDao : EntityDao<TreinosEntity>() {

    @Query("SELECT * FROM treinos")
    abstract fun getAll(): List<TreinosEntity>

    @Query("SELECT * FROM treinos WHERE id_treino = :idTreino")
    abstract suspend fun getByIdTreino(idTreino:Long): List<TreinosEntity>

    @Query("SELECT id_treino FROM treinos ORDER BY id_treino DESC LIMIT 1")
    abstract  fun ultimoTreinoStream(): Flow<Long?>

    @Query("SELECT id_treino FROM treinos ORDER BY id_treino DESC LIMIT 1")
    abstract suspend fun ultimoTreino(): Long?

    @Query("DELETE FROM treinos")
    abstract suspend fun deleteAll()

}


package br.com.reconhecimento.database.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.reconhecimento.database.entity.ErroEntity
import br.com.reconhecimento.database.entity.TreinosEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ErroDao : EntityDao<ErroEntity>() {

    @Query("SELECT * FROM erro ORDER BY id_treino DESC LIMIT 1")
    abstract fun ultimoTreinoStream(): Flow<ErroEntity?>


    @Query("SELECT * FROM erro WHERE id_treino = :idTreino and erro > 0.0000000 LIMIT 100")
    abstract suspend fun getByIdTreino(idTreino:Long): List<ErroEntity>

    @Query("DELETE FROM erro")
    abstract suspend fun deleteAll()

}


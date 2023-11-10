package br.com.reconhecimento.database.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.reconhecimento.database.entity.FuncaoAtivacaoEntity
import br.com.reconhecimento.database.entity.TreinosEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FuncaoAtivacaoDao : EntityDao<FuncaoAtivacaoEntity>() {

    @Query("SELECT * FROM funcao_ativacao")
    abstract fun getAll(): Flow<List<FuncaoAtivacaoEntity>>

    @Query("SELECT * FROM funcao_ativacao WHERE id_treino = :idTreino ORDER BY id DESC LIMIT 1")
    abstract fun getUltimaDoTreino(idTreino: Long): FuncaoAtivacaoEntity

    @Query("SELECT * FROM funcao_ativacao WHERE id_treino = :idTreino")
    abstract suspend fun getByIdTreino(idTreino: Long): List<FuncaoAtivacaoEntity>


}


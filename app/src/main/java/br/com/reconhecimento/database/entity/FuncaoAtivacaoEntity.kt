package br.com.reconhecimento.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.reconhecimento.data.model.Erro
import br.com.reconhecimento.data.model.FuncaoAtivacao
import br.com.reconhecimento.data.model.Treinos

@Entity(tableName = "funcao_ativacao")
data class FuncaoAtivacaoEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override var id: Long,
    @ColumnInfo(name = "id_treino") val idTreino: Long,
    @ColumnInfo(name = "vetor") val vetor: Double,
    @ColumnInfo(name = "saida") val saida: Double,
) : AppEntity

fun FuncaoAtivacaoEntity.toModel() = FuncaoAtivacao(
    idTreino = idTreino,
    vetor = vetor,
    saida = saida,
)
package br.com.reconhecimento.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.reconhecimento.data.model.Erro
import br.com.reconhecimento.data.model.Treinos

@Entity(tableName = "erro")
data class ErroEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override var id: Long,
    @ColumnInfo(name = "id_treino") val idTreino: Long,
    @ColumnInfo(name = "erro") val erro: Double,
) : AppEntity

fun ErroEntity.toModel() = Erro(
    idTreino = idTreino,
    erro = erro
)
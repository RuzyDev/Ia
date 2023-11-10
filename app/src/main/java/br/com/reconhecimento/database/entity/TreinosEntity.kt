package br.com.reconhecimento.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.reconhecimento.data.model.Treinos

@Entity(tableName = "treinos")
data class TreinosEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override var id: Long,
    @ColumnInfo(name = "id_treino") val idTreino: Long,
    @ColumnInfo(name = "neuronio") val neuronio: Long,
    @ColumnInfo(name = "bias_v") val biasV: Double,
    @ColumnInfo(name = "peso_v") val pesoV: Double,
    @ColumnInfo(name = "bias_w") val biasW: Double,
    @ColumnInfo(name = "peso_w") val pesoW: Double,
) : AppEntity

fun TreinosEntity.toModel() = Treinos(
    idTreino = idTreino,
    neuronio = neuronio,
    biasV = biasV,
    pesoV = pesoV,
    biasW = biasW,
    pesoW = pesoW,
)
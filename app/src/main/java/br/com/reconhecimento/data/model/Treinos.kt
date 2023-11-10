package br.com.reconhecimento.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.reconhecimento.database.entity.AppEntity


data class Treinos(
    val idTreino: Long,
    val neuronio: Long,
    val biasV: Double,
    val pesoV: Double,
    val biasW: Double,
    val pesoW: Double,
)

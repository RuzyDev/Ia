package br.com.reconhecimento.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.reconhecimento.data.model.Erro
import br.com.reconhecimento.data.model.Treinos


data class FuncaoAtivacao(
    val idTreino: Long,
    val vetor: Double,
    val saida: Double,
)
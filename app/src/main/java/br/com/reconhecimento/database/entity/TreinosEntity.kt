package br.com.reconhecimento.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lgpdVisitante")
data class TreinosEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override var id: Long
) : AppEntity

package br.com.arcom.signpad.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.arcom.signpad.data.model.LgpdVisitante
import br.com.arcom.signpad.util.formatDateTime
import java.time.LocalDateTime

@Entity(tableName = "lgpdVisitante")
data class LgpdVisitanteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override var id: Long,
    @ColumnInfo(name = "cpf") val cpf: Long,
    @ColumnInfo(name = "nome") val nome: String,
    @ColumnInfo(name = "dataAss") val dataAss: String,
    @ColumnInfo(name = "pdfBase64") val pdfBase64: String,
) : AppEntity

fun LgpdVisitanteEntity.asExternalModel() = LgpdVisitante(
    cpf,
    nome,
    dataAss.formatDateTime("dd-MM-yyyy HH:mm:ss"),
    pdfBase64,
)
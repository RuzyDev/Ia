package br.com.reconhecimento.util

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.util.Base64
import java.nio.charset.StandardCharsets
import java.time.LocalDate

object TokenUtil {

    fun getToken(context: Context): String? {
        val cursor: Cursor? = context.contentResolver
            .query(Uri.parse("content://br.com.arcom.id.provider/token"), null, null, null, null)
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return Base64.encodeToString(
                        (cursor.getString(0)
                            .toString() + ":").toByteArray(StandardCharsets.ISO_8859_1), Base64.NO_WRAP
                    )
                }
            } finally {
                if (!cursor.isClosed) cursor.close()
            }
        }
        return ""
    }
}

fun clearArcomId(context: Context){
    val intent = context.packageManager.getLaunchIntentForPackage("br.com.arcom.id")
    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent?.data = Uri.parse("arcomid://arcom.com.br/clear-data")
    context.startActivity(intent)
}

fun getSenhaDoDia(): Long {
    val now = LocalDate.now()

    val lAno = now.year.toLong()
    val sSenha = now.formatData("ddMMyyyy").replace("/", "")
    var senha = sSenha.toLong()
    senha = senha xor lAno
    senha = senha shr 1

    return senha
}
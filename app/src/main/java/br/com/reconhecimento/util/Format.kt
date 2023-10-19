package br.com.reconhecimento.util

import android.graphics.Bitmap
import android.util.Base64
import java.io.*

fun String.asNumber(): Boolean {
    return if (this.isNotEmpty()) {
        this.all { char -> char.isDigit() }
    }else{
        true
    }
}

fun String.getNumbers() = this.filter { it.isDigit() }.toLongOrNull()

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}

fun String?.useNonBreakingSpace() = this.orEmpty()
    .replace(
        Constants.REGULAR_SPACE_CHARACTER,
        Constants.NON_BREAKABLE_SPACE_UNICODE
    )

fun formatBytes(bytes: Long): String {
    return if (bytes < 1024) {
        "$bytes b"
    } else if (bytes in 1024..1048575) {
        "${bytes / 1024} kB"
    } else {
        "${bytes / 1048576} MB"
    }
}

object Constants {
    const val REGULAR_SPACE_CHARACTER = ' '
    const val NON_BREAKABLE_SPACE_UNICODE = '\u00A0'
}

fun String?.getPrimeiroNome(): String? {
    val nomeArray = this?.split(" ") ?: return null
    return if (nomeArray.size > 1){
        nomeArray[0]
    }else{
        "Acessor"
    }
}

fun encodeFileToBase64Binary(yourFile: File): String {
    val size = yourFile.length().toInt()
    val bytes = ByteArray(size)
    try {
        val buf =
            BufferedInputStream(FileInputStream(yourFile))
        buf.read(bytes, 0, bytes.size)
        buf.close()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}
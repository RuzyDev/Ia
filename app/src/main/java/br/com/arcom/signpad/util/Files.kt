package br.com.arcom.signpad.util

import android.content.Context
import java.io.File

class Files(context: Context) {

    val dowloads: File = File(context.filesDir, "downloads")

    val cache: File = context.cacheDir

    fun gerarDiretorios() {
        dowloads.mkdir()
    }
}
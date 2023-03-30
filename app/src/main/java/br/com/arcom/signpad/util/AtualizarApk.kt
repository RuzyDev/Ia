package br.com.arcom.signpad.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import br.com.arcom.signpad.BuildConfig
import java.io.File

fun atualizarApk(context: Context, file: String) {
    if( File(file).exists() ) {
        val uri = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".fileprovider",
            File(file)
        )
        val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
        intent.data = uri
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        context.startActivity(intent)
    }
}

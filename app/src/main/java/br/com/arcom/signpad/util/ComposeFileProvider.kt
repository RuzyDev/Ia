package br.com.arcom.signpad.util


import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Base64
import androidx.core.content.FileProvider.getUriForFile
import br.com.arcom.signpad.BuildConfig
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime


fun getImageUri(context: Context): Pair<File, Uri> {
    val directory = File(context.cacheDir, "images")
    directory.mkdirs()

    val file = File.createTempFile(
        "selected_image_",
        ".jpg",
        directory
    )

    val authority = BuildConfig.APPLICATION_ID + ".fileprovider"

    return Pair(
        file, getUriForFile(
            context,
            authority,
            file,
        )
    )
}

fun salvarImagemDiretorioFotos(uri: Uri, nomeArquivo: String, context: Context): File {

    val directory = File(context.cacheDir, "images")
    directory.mkdirs()

    val file = File(directory.absolutePath, nomeArquivo)
    if (!file.exists()) {
        file.createNewFile()
    }
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = FileOutputStream(file)
    inputStream.use { input ->
        outputStream.use { output ->
            input?.copyTo(output)
        }
    }
    ImageRotationUtil.rotateAndCompressImage(file)

    return file
}

fun salvarAssinatura(myBitmap: Bitmap, context: Context): String {
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    val fileName = "assinatura_${LocalDateTime.now()}.jpg"

    val file = File(dir, fileName)

    val outputStream = FileOutputStream(file)
    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

    outputStream.flush()
    outputStream.close()

    return file.absolutePath
}

fun convertPdf(file: File): Bitmap? {
    val pageNumber = 0

    val renderer = PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY))

    val page = renderer.openPage(pageNumber)

    val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)

    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

    page.close()
    renderer.close()

    return bitmap
}

fun Context.base64ToBitmap(base64String: String?): Bitmap? {
    val filePath = this.filesDir
    val fileName = "visitante.pdf"
    val file = File(filePath, fileName)
    val bitmap = try {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(decodedBytes)
        fileOutputStream.close()
        convertPdf(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    file.delete()
    return bitmap
}

package br.com.arcom.signpad.ui.assinatura.navigation

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.SavedStateHandle

data class AssinaturaArgs(val nome: String, val cpf: Long, val foto: Bitmap)
package br.com.arcom.signpad

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import br.com.arcom.signpad.ui.app.SignpadApp
import dagger.hilt.android.AndroidEntryPoint

private const val PERMISSION_REQUEST_CODE = 1

@AndroidEntryPoint
class MainActivity : AppActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignpadApp(calculateWindowSizeClass(this))
        }
    }
}

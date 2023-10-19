package br.com.arcom.signpad.ui.assinatura.navigation

import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.arcom.signpad.ui.assinatura.AssinaturaRoute
import br.com.arcom.signpad.ui.assinatura.navigation.AssinaturaNavigation.assinaturaArgs
import br.com.arcom.signpad.ui.navigation.SignpadNavigationDestination
import com.google.gson.Gson

object AssinaturaNavigation : SignpadNavigationDestination {
    val assinaturaArgs = "assinaturaArgs"
    override val route = "assinatura_route"
    override val destination = "assinatura_destination"
}

fun NavController.navigateToAssinatura(args: AssinaturaArgs) {
    val gson = Gson().toJson(args)
    val encodedAssinatura = Uri.encode(gson)
    this.navigate("${AssinaturaNavigation.route}/$encodedAssinatura")
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.assinatura(
    onBackClick: () -> Unit,
    navigateToPdf: (String, Long) -> Unit,
) {
    composable(
        route = "${AssinaturaNavigation.route}/{$assinaturaArgs}",
        arguments = listOf(
            navArgument(assinaturaArgs) { type = NavType.StringType }
        )
    ) {
        AssinaturaRoute(
            onBackClick = onBackClick,
            navigateToPdf = navigateToPdf
        )
    }
}
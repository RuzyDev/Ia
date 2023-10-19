package br.com.arcom.signpad.ui.visitantepdf.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.arcom.signpad.ui.navigation.SignpadNavigationDestination
import br.com.arcom.signpad.ui.pdf.navigation.PdfNavigation
import br.com.arcom.signpad.ui.visitantepdf.VisitantePdfRoute
import br.com.arcom.signpad.ui.visitantescadastrados.VisitantesCadastradosRoute

private const val cpfArgument = "cpfArgument"

object VisitantePdfNavigation : SignpadNavigationDestination {
    override val route = "visitante_pdf_route/{$cpfArgument}"
    override val destination = "visitante_pdf_destination"

    fun getArguments(handle: SavedStateHandle): Long = checkNotNull(handle[cpfArgument])
}

fun NavController.navigateToVisitantePdf(cpf: Long) {
    this.navigate("visitante_pdf_route/$cpf")
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.visitantePdf(
    onBackClick: () -> Unit
) {
    composable(
        route = VisitantePdfNavigation.route,
        arguments = listOf(
            navArgument(cpfArgument) { type = NavType.LongType }
        )
    ) {
        VisitantePdfRoute(onBackClick = onBackClick)
    }
}
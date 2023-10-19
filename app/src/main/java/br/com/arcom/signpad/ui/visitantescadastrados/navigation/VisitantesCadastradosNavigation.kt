package br.com.arcom.signpad.ui.visitantescadastrados.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.arcom.signpad.ui.navigation.SignpadNavigationDestination
import br.com.arcom.signpad.ui.visitantescadastrados.VisitantesCadastradosRoute

object VisitantesCadastradosNavigation : SignpadNavigationDestination {
    override val route = "visitantes_cadastrados_route"
    override val destination = "visitantes_cadastrados_destination"
}

fun NavController.navigateToVisitantesCadastrados() {
    this.navigate(VisitantesCadastradosNavigation.route)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.visitantesCadastrados(
    onBackClick: () -> Unit,
    navigateToVisitantePdf: (Long) -> Unit,
) {
    composable(
        route = VisitantesCadastradosNavigation.route
    ) {
        VisitantesCadastradosRoute(
            onBackClick = onBackClick,
            navigateToVisitantePdf = navigateToVisitantePdf,
        )
    }
}
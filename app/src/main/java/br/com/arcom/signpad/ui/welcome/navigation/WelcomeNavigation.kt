package br.com.arcom.signpad.ui.welcome.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.arcom.signpad.ui.dadosvisitante.navigation.navigateToDadosVisitante
import br.com.arcom.signpad.ui.navigation.SignpadNavigationDestination
import br.com.arcom.signpad.ui.welcome.WelcomeRoute

object WelcomeNavigation : SignpadNavigationDestination {
    override val route = "welcome_route"
    override val destination = "welcome_destination"
}

fun NavController.navigateToWelcome() {
    this.navigate(WelcomeNavigation.route)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.welcome(
    navigateToDadosVisitante: () -> Unit,
    navigateToVisitantesCadastrados: () -> Unit,
) {
    composable(
        route = WelcomeNavigation.route
    ) {
        WelcomeRoute(
            navigateToDadosVisitante = navigateToDadosVisitante,
            navigateToVisitantesCadastrados = navigateToVisitantesCadastrados
        )
    }
}
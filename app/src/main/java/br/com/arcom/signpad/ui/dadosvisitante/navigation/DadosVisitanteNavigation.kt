package br.com.arcom.signpad.ui.dadosvisitante.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.arcom.signpad.ui.assinatura.navigation.AssinaturaArgs
import br.com.arcom.signpad.ui.dadosvisitante.DadosVisitanteRoute
import br.com.arcom.signpad.ui.navigation.SignpadNavigationDestination

object DadosVisitanteNavigation : SignpadNavigationDestination {
    override val route = "dados_visitante_route"
    override val destination = "dados_visitante_destination"
}

fun NavController.navigateToDadosVisitante() {
    this.navigate(DadosVisitanteNavigation.route)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.dadosVisitante(
    onBackClick: () -> Unit,
    navigateToAssinatura: (AssinaturaArgs) -> Unit
) {
    composable(
        route = DadosVisitanteNavigation.route
    ) {
        DadosVisitanteRoute(onBackClick = onBackClick,navigateToAssinatura = navigateToAssinatura)
    }
}
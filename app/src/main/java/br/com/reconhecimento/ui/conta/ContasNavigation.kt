package br.com.reconhecimento.ui.conta

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.reconhecimento.ui.navigation.ReconhecimentoNavigation

object ContasNavigation : ReconhecimentoNavigation {
    override val route = "contas_route"
    override val destination = "contas_destination"
}

fun NavController.navigateToContas() {
    this.navigate(ContasNavigation.route)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.contas() {
    composable(
        route = ContasNavigation.route
    ) {
        ContasRoute()
    }
}
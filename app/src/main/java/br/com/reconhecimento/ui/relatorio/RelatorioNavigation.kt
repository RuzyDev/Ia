package br.com.reconhecimento.ui.relatorio

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.reconhecimento.ui.navigation.ReconhecimentoNavigation

object RelatorioNavigation : ReconhecimentoNavigation {
    override val route = "relatorio_route"
    override val destination = "relatorio_destination"
}

fun NavController.navigateToRelatorio() {
    this.navigate(RelatorioNavigation.route)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.relatorio() {
    composable(
        route = RelatorioNavigation.route
    ) {
        RelatorioRoute()
    }
}
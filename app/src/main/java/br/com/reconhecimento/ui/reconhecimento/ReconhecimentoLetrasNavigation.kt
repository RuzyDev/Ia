package br.com.reconhecimento.ui.reconhecimento

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.reconhecimento.ui.navigation.ReconhecimentoNavigation
import br.com.reconhecimento.ui.reconhecimento.ReconhecimentoLetrasRoute

object ReconhecimentoLetrasNavigation : ReconhecimentoNavigation {
    override val route = "dados_visitante_route"
    override val destination = "dados_visitante_destination"
}

fun NavController.navigateToReconhecimentoLetras() {
    this.navigate(ReconhecimentoLetrasNavigation.route)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.reconhecimentoLetras() {
    composable(
        route = ReconhecimentoLetrasNavigation.route
    ) {
        ReconhecimentoLetrasRoute()
    }
}
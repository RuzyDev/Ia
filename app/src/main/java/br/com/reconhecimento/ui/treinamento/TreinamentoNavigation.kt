package br.com.reconhecimento.ui.treinamento

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.reconhecimento.ui.navigation.ReconhecimentoNavigation

object TreinamentoNavigation : ReconhecimentoNavigation {
    override val route = "dados_visitante_route"
    override val destination = "dados_visitante_destination"
}

fun NavController.navigateToTreinamento() {
    this.navigate(TreinamentoNavigation.route)
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.treinamento() {
    composable(
        route = TreinamentoNavigation.route
    ) {
        TreinamentoRoute()
    }
}
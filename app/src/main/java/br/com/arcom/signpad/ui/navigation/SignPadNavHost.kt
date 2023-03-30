package br.com.arcom.signpad.ui.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.arcom.signpad.ui.assinatura.navigation.assinatura
import br.com.arcom.signpad.ui.assinatura.navigation.navigateToAssinatura
import br.com.arcom.signpad.ui.dadosvisitante.navigation.dadosVisitante
import br.com.arcom.signpad.ui.dadosvisitante.navigation.navigateToDadosVisitante
import br.com.arcom.signpad.ui.pdf.navigation.navigateToPdf
import br.com.arcom.signpad.ui.pdf.navigation.pdf
import br.com.arcom.signpad.ui.visitantescadastrados.navigation.navigateToVisitantesCadastrados
import br.com.arcom.signpad.ui.visitantescadastrados.navigation.visitantesCadastrados
import br.com.arcom.signpad.ui.welcome.navigation.WelcomeNavigation
import br.com.arcom.signpad.ui.welcome.navigation.navigateToWelcome
import br.com.arcom.signpad.ui.welcome.navigation.welcome
import kotlin.reflect.KFunction1

@Composable
fun SignPadNavHost(
    navController: NavHostController,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = WelcomeNavigation.route,
    onBackClickWithDestination: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        welcome(
            navigateToDadosVisitante = navController::navigateToDadosVisitante,
            navigateToVisitantesCadastrados = navController::navigateToVisitantesCadastrados
        )
        dadosVisitante(
            onBackClick,
            navigateToAssinatura = navController::navigateToAssinatura
        )
        assinatura(onBackClick, navigateToPdf = navController::navigateToPdf)
        pdf(navigateToWelcome = { onBackClickWithDestination(WelcomeNavigation.route) })
        visitantesCadastrados(onBackClick)
    }

}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultSignPadEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultSignPadExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultSignPadPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultSignPadPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}

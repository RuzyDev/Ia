package br.com.arcom.signpad.ui.pdf.navigation

import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.*
import androidx.navigation.compose.composable
import br.com.arcom.signpad.ui.assinatura.navigation.AssinaturaNavigation
import br.com.arcom.signpad.ui.navigation.SignpadNavigationDestination
import br.com.arcom.signpad.ui.pdf.PdfRoute
import br.com.arcom.signpad.ui.pdf.navigation.PdfNavigation.cpfArg
import br.com.arcom.signpad.ui.pdf.navigation.PdfNavigation.pdfArg

object PdfNavigation : SignpadNavigationDestination {
    val pdfArg = "pdfArg"
    val cpfArg = "cpfArg"
    override val route = "pdf_route"
    override val destination = "pdf_destination"
}

fun NavController.navigateToPdf(pdfPath: String,cpf: Long) {
    val encodedPdfPath = Uri.encode(pdfPath)
    val encodedCpf = Uri.encode(cpf.toString())
    this.navigate("${PdfNavigation.route}/$encodedPdfPath/$encodedCpf")
}

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.pdf(
    navigateToWelcome: () -> Unit
) {
    composable(
        route = "${PdfNavigation.route}/{$pdfArg}/{$cpfArg}",
        arguments = listOf(
            navArgument(pdfArg) { type = NavType.StringType },
            navArgument(cpfArg) { type = NavType.LongType },
        )
    ) {
        PdfRoute( navigateToWelcome = navigateToWelcome)
    }
}
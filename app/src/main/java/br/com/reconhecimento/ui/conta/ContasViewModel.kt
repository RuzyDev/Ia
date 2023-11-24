package br.com.reconhecimento.ui.conta

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.reconhecimento.data.model.Fonte
import br.com.reconhecimento.data.model.Letras
import br.com.reconhecimento.data.model.defaultPeso
import br.com.reconhecimento.data.model.saidasDesejadas
import br.com.reconhecimento.ui.reconhecimento.UiState
import br.com.reconhecimento.util.ObservableLoadingCounter
import br.com.reconhecimento.util.UiMessage
import br.com.reconhecimento.util.UiMessageManager
import br.com.reconhecimento.util.getNumbers
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.BigDecimal
import java.util.Stack
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.pow

@HiltViewModel
class ContasViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var _expressao = MutableStateFlow<Expressao?>(null)

    val loading = ObservableLoadingCounter()
    val uiMessageManager = UiMessageManager()

    val contaUiState: StateFlow<ContasResults> =
        combine(
            uiMessageManager.message,
            loading.observable,
            _expressao,
            ::ContasResults
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ContasResults.Empty,
        )


    fun recognizeText(btp: Bitmap, callback: () -> Unit) {
        viewModelScope.launch {
            loading.addLoader()
            _expressao.emit(null)
            FirebaseVision.getInstance().onDeviceTextRecognizer.processImage(
                FirebaseVisionImage.fromBitmap(
                    btp
                )
            ).addOnSuccessListener { firebaseVisionText ->
                viewModelScope.launch {
                    val text = firebaseVisionText.textBlocks.flatMap { it.lines }.lastOrNull()?.text
                    if (text == null) {
                        emitMessage("Erro ao analisar imagem!")
                    } else {
                        try {
                            val resultado = ExpressionBuilder(text).build().evaluate()
                            _expressao.emit(
                                Expressao(text, resultado)
                            )
                            callback()
                        } catch (e: Exception) {
                            _expressao.emit(
                                null
                            )
                            emitMessage("Erro ao analisar expressão!")
                        }
                    }
                }
            }.addOnFailureListener { e ->
                e.printStackTrace()
                emitMessage("Erro ao analisar imagem!")
            }
            loading.removeLoader()
        }
    }

    fun emitMessage(message: String) {
        viewModelScope.launch {
            uiMessageManager.emitMessage(UiMessage(message))
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            uiMessageManager.clearAll()
        }
    }

}

data class ContasResults(
    val uiMessage: UiMessage? = null,
    val loading: Boolean = false,
    val expressao: Expressao? = null
) {
    companion object {
        val Empty = ContasResults()
    }
}

fun calcularExpressao(expressao: String): BigDecimal? {
    val postfix = infixToPostfix(expressao)
    return if (postfix != null) {
        avaliarPostfix(postfix)
    } else {
        null
    }
}

fun infixToPostfix(expressao: String): String? {
    val output = StringBuilder()
    val operadores = Stack<Char>()

    for (token in expressao) {
        when {
            token.isDigit() -> output.append(token)
            token == '(' -> operadores.push(token)
            token == ')' -> {
                while (operadores.isNotEmpty() && operadores.peek() != '(') {
                    output.append(operadores.pop())
                }
                operadores.pop() // Remover o '('
            }

            token in setOf('+', '-', '*', '/', '%') -> {
                while (operadores.isNotEmpty() && getPrecedencia(operadores.peek()) >= getPrecedencia(
                        token
                    )
                ) {
                    output.append(operadores.pop())
                }
                operadores.push(token)
            }

            token.isWhitespace() -> {
                // Ignorar espaços em branco
            }

            else -> return null // Caractere não reconhecido
        }
    }

    while (operadores.isNotEmpty()) {
        output.append(operadores.pop())
    }

    return output.toString()
}

fun avaliarPostfix(postfix: String): BigDecimal {
    val pilha = Stack<BigDecimal>()

    for (token in postfix) {
        if (token.isDigit()) {
            pilha.push(BigDecimal(token.toString()))
        } else {
            val b = pilha.pop()
            val a = pilha.pop()

            val resultado = when (token) {
                '+' -> a + b
                '-' -> a - b
                '*' -> a * b
                '/' -> a / b
                '%' -> a % b
                else -> throw IllegalArgumentException("Operador não reconhecido: $token")
            }

            pilha.push(resultado)
        }
    }

    return pilha.pop()
}

fun getPrecedencia(operador: Char): Int {
    return when (operador) {
        '+', '-' -> 1
        '*', '/', '%' -> 2
        else -> 0
    }
}

data class Expressao(
    val conta: String,
    val resultado: Double
)
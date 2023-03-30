package br.com.arcom.signpad.ui.commons

import android.content.Context
import br.com.arcom.repplus.ui.commons.UiMessage
import br.com.arcom.repplus.ui.commons.UiMessageManager
import br.com.arcom.signpad.data.domain.InvokeError
import br.com.arcom.signpad.data.domain.InvokeStarted
import br.com.arcom.signpad.data.domain.InvokeStatus
import br.com.arcom.signpad.data.domain.InvokeSuccess
import br.com.arcom.signpad.util.Logger
import br.com.arcom.signpad.util.clearArcomId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    logger: Logger? = null,
    uiMessageManager: UiMessageManager? = null
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            logger?.i(status.throwable)
            status.throwable.printStackTrace()
            uiMessageManager?.emitMessage(UiMessage("Ocorreu um erro!"))
            counter.removeLoader()
        }
        else -> { counter.removeLoader() }
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    logger: Logger? = null,
    uiMessageManager: UiMessageManager? = null,
    callback: (() -> Unit)? = null,
    callbackError: ((throwable: Throwable) -> Unit)? = null
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> {
            counter.removeLoader()
            if (callback != null) {
                callback()
            }
        }
        is InvokeError -> {
            logger?.i(status.throwable)
            uiMessageManager?.emitMessage(UiMessage("Ocorreu um erro!"))
            counter.removeLoader()
            if (callbackError != null) {
                callbackError(status.throwable)
            }
        }
        else -> {
            counter.removeLoader()
        }
    }
}
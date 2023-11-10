package br.com.reconhecimento.util

import android.util.Log
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
            Log.d("exception", status.throwable.message ?: "")
            uiMessageManager?.emitMessage(UiMessage(status.throwable.message ?: "Erro"))
            counter.removeLoader()
        }
        else -> {
            counter.removeLoader()
        }
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
            if (callback != null) {
                callback()
            }
            counter.removeLoader()
        }
        is InvokeError -> {
            logger?.i(status.throwable)
            status.throwable.printStackTrace()
            uiMessageManager?.emitMessage(UiMessage(status.throwable.message ?: ""))
            if (callbackError != null) {
                callbackError(status.throwable)
            }
            counter.removeLoader()
        }
        else -> {
            counter.removeLoader()
        }
    }
}

suspend fun Flow<InvokeStatus>.collectStatusWithNavigation(
    counter: ObservableLoadingCounter,
    logger: Logger? = null,
    uiMessageManager: UiMessageManager? = null,
    navigation: () -> Unit,
    callbackError: ((throwable: Throwable) -> Unit)? = null
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> {
            navigation()
        }
        is InvokeError -> {
            logger?.i(status.throwable)
            status.throwable.printStackTrace()
            uiMessageManager?.emitMessage(UiMessage(status.throwable.message ?: ""))
            if (callbackError != null) {
                callbackError(status.throwable)
            }
            counter.removeLoader()
        }
        else -> {
            counter.removeLoader()
        }
    }
}
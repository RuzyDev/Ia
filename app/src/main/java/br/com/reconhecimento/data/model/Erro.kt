package br.com.reconhecimento.data.model

import br.com.reconhecimento.data.repository.TreinamentoRepository
import br.com.reconhecimento.di.AppCoroutineDispatchers
import br.com.reconhecimento.util.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class Erro (
    val idTreino: Long,
    val erro: Double,
)

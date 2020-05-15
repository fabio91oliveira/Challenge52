package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.organizer.domain.usecase.SortTransactionsUseCase
import oliveira.fabio.challenge52.presentation.vo.Transaction
import java.util.*

internal class SortTransactionsUseCaseImpl : SortTransactionsUseCase {
    override suspend fun invoke(transactions: LinkedList<Transaction>) {
        withContext(Dispatchers.Default) {
            transactions.sortByDescending { it.id }
        }
    }
}
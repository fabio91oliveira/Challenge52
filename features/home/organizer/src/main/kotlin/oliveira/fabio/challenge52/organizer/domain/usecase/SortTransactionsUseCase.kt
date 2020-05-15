package oliveira.fabio.challenge52.organizer.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Transaction
import java.util.*

interface SortTransactionsUseCase {
    suspend operator fun invoke(transaction: LinkedList<Transaction>)
}
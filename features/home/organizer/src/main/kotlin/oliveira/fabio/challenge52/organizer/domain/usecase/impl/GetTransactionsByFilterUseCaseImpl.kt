package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.organizer.domain.usecase.GetTransactionsByFilterUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.SortTransactionsUseCase
import oliveira.fabio.challenge52.organizer.presentation.enums.FilterEnum
import oliveira.fabio.challenge52.presentation.vo.Balance
import java.util.*

internal class GetTransactionsByFilterUseCaseImpl(
    private val sortTransactionsUseCase: SortTransactionsUseCase
) : GetTransactionsByFilterUseCase {
    override suspend fun invoke(
        balance: Balance,
        value: String
    ) {
        withContext(Dispatchers.Default) {
            balance.filter = value
            balance.transactionsFiltered = LinkedList(balance.allTransactions)

            if (value != FilterEnum.ALL.tag) {
                balance.transactionsFiltered.retainAll { it.typeTransaction.value == value }
            }

            sortTransactionsUseCase(balance.transactionsFiltered)
        }
    }
}
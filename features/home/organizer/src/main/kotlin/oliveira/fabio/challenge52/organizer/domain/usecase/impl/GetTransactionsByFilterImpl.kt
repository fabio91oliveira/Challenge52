package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.organizer.domain.usecase.GetTransactionsByFilter
import oliveira.fabio.challenge52.organizer.presentation.vo.FilterEnum
import oliveira.fabio.challenge52.presentation.vo.Balance

internal class GetTransactionsByFilterImpl : GetTransactionsByFilter {
    override suspend fun invoke(
        balance: Balance,
        value: String
    ) {
        withContext(Dispatchers.Default) {
            balance.filter = value

            if (value == FilterEnum.ALL.tag) {
                balance.transactionsFiltered.clear()
                balance.transactionsFiltered.addAll(balance.allTransactions)
            } else {
                val typedTransactions =
                    balance.allTransactions.filter { it.typeTransaction.value == value }
                balance.transactionsFiltered.clear()
                balance.transactionsFiltered.addAll(typedTransactions)
            }
        }
    }
}
package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.organizer.domain.usecase.UpdateBalanceAfterRemoveTransactionUseCase
import oliveira.fabio.challenge52.presentation.vo.Balance
import oliveira.fabio.challenge52.presentation.vo.Transaction
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum

internal class UpdateBalanceAfterRemoveTransactionUseCaseImpl(
) : UpdateBalanceAfterRemoveTransactionUseCase {
    override suspend fun invoke(balance: Balance, transaction: Transaction) {
        withContext(Dispatchers.Default) {
            updateLists(balance, transaction)
            updateValues(balance, transaction)
        }
    }

    private fun updateLists(
        balance: Balance,
        transaction: Transaction
    ) {
        with(balance) {
            allTransactions.retainAll { it.id != transaction.id }
            transactionsFiltered.retainAll { it.id != transaction.id }
        }
    }

    private fun updateValues(
        balance: Balance,
        transaction: Transaction
    ) {
        with(balance) {
            if (allTransactions.isEmpty()) {
                totalAllFilter = 0
                totalIncomeFilter = 0
                totalSpentFilter = 0

                total = ZERO_MONEY
                totalIncomes = ZERO_MONEY
                totalSpent = ZERO_MONEY
            } else {
                totalAllFilter--

                when (transaction.typeTransaction) {
                    TypeTransactionEnum.INCOME -> {
                        totalIncomes -= transaction.money
                        totalIncomeFilter--
                    }
                    TypeTransactionEnum.SPENT -> {
                        totalSpent -= transaction.money
                        totalSpentFilter--
                    }
                }
                total = totalIncomes - totalSpent
            }
        }
    }

    companion object {
        private const val ZERO_MONEY = 0.toDouble()
    }
}

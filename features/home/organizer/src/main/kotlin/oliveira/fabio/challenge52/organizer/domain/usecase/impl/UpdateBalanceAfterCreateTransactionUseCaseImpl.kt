package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.organizer.domain.usecase.UpdateBalanceAfterCreateTransactionUseCase
import oliveira.fabio.challenge52.presentation.vo.Balance
import oliveira.fabio.challenge52.presentation.vo.Transaction
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum

internal class UpdateBalanceAfterCreateTransactionUseCaseImpl(
) : UpdateBalanceAfterCreateTransactionUseCase {
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
            allTransactions.push(transaction)
            transactionsFiltered.push(transaction)
        }
    }

    private fun updateValues(
        balance: Balance,
        transaction: Transaction
    ) {
        with(balance) {
            totalAllFilter++

            when (transaction.typeTransaction) {
                TypeTransactionEnum.INCOME -> {
                    totalIncomes += transaction.money
                    totalIncomeFilter++
                }
                TypeTransactionEnum.SPENT -> {
                    totalSpent += transaction.money
                    totalSpentFilter++
                }
            }
            total = totalIncomes - totalSpent
        }
    }
}
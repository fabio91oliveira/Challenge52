package oliveira.fabio.challenge52.data.mapper.impl

import oliveira.fabio.challenge52.data.mapper.BalanceMapper
import oliveira.fabio.challenge52.persistence.model.entity.BalanceWithTransactionsEntity
import oliveira.fabio.challenge52.persistence.model.enums.TransactionTypeEnum
import oliveira.fabio.challenge52.presentation.vo.Balance
import oliveira.fabio.challenge52.presentation.vo.Transaction
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum
import java.util.*

internal class BalanceMapperImpl :
    BalanceMapper {
    override fun invoke(
        balanceWithTransactionsEntity: BalanceWithTransactionsEntity?,
        date: Date
    ): Balance {
        balanceWithTransactionsEntity?.also {
            var totalIncomes = 0.0
            var totalSpent = 0.0

            var totalFilterIncomes = 0
            var totalFilterSpent = 0

            val transactions = LinkedList<Transaction>().apply {
                balanceWithTransactionsEntity.transactions.forEach {
                    if (it.type == TransactionTypeEnum.INCOME) {
                        totalIncomes += it.money
                        totalFilterIncomes++
                    } else {
                        totalSpent += it.money
                        totalFilterSpent++
                    }
                    add(
                        Transaction(
                            id = it.id,
                            idBalance = balanceWithTransactionsEntity.balance.id,
                            icoResource = it.icoResource,
                            description = it.description,
                            date = it.date,
                            money = it.money,
                            typeTransaction = getType(it.type)
                        )
                    )
                }
            }
            return Balance(
                id = balanceWithTransactionsEntity.balance.id,
                date = balanceWithTransactionsEntity.balance.startDate,
                currentLocale = balanceWithTransactionsEntity.balance.currentLocale,
                transactionsFiltered = transactions,
                allTransactions = LinkedList(transactions),
                total = totalIncomes - totalSpent,
                totalIncomes = totalIncomes,
                totalSpent = totalSpent,
                totalAllFilter = totalFilterIncomes + totalFilterSpent,
                totalIncomeFilter = totalFilterIncomes,
                totalSpentFilter = totalFilterSpent,
                isHide = balanceWithTransactionsEntity.balance.isHide
            )
        }
        return Balance(date = date)
    }

    private fun getType(transactionTypeEnum: TransactionTypeEnum): TypeTransactionEnum {
        return when (transactionTypeEnum) {
            TransactionTypeEnum.INCOME -> TypeTransactionEnum.INCOME
            else -> TypeTransactionEnum.SPENT
        }
    }
}
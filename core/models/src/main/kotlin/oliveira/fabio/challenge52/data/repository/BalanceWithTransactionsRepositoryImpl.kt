package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.data.mapper.BalanceMapper
import oliveira.fabio.challenge52.domain.repository.BalanceWithTransactionsRepository
import oliveira.fabio.challenge52.persistence.datasource.BalanceWithTransactionsLocalDataSource
import oliveira.fabio.challenge52.presentation.vo.Balance
import java.util.*

internal class BalanceWithTransactionsRepositoryImpl(
    private val balanceWithTransactionsLocalDataSource: BalanceWithTransactionsLocalDataSource,
    private val balanceMapper: BalanceMapper
) : BalanceWithTransactionsRepository {
    override fun getBalanceByDate(date: Date): Balance {
        return balanceMapper(
            balanceWithTransactionsLocalDataSource.getBalanceWithTransactionsByDate(date),
            date
        )
    }
}
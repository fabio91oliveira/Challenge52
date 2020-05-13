package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.BalanceWithTransactionsDao
import oliveira.fabio.challenge52.persistence.datasource.BalanceWithTransactionsLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.BalanceWithTransactionsEntity
import java.util.*

internal class BalanceWithTransactionsLocalDataSourceImpl(
    private val balanceWithTransactionsDao: BalanceWithTransactionsDao
) :
    BalanceWithTransactionsLocalDataSource {
    override fun getBalanceWithTransactionsByDate(date: Date) =
        balanceWithTransactionsDao.getBalanceByDate(date)
}
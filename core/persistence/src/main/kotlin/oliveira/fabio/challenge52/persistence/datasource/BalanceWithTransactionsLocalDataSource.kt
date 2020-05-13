package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.entity.BalanceWithTransactionsEntity
import java.util.*

interface BalanceWithTransactionsLocalDataSource {
    fun getBalanceWithTransactionsByDate(date: Date): BalanceWithTransactionsEntity?
}
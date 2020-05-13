package oliveira.fabio.challenge52.persistence.datasource.impl

import oliveira.fabio.challenge52.persistence.dao.TransactionDao
import oliveira.fabio.challenge52.persistence.datasource.TransactionLocalDataSource
import oliveira.fabio.challenge52.persistence.model.entity.TransactionEntity

internal class TransactionLocalDataSourceImpl(private val transactionDao: TransactionDao) :
    TransactionLocalDataSource {
    override fun addTransaction(transactionEntity: TransactionEntity) =
        transactionDao.addTransaction(transactionEntity)

    override fun removeTransactionByIdBalance(idBalance: Long, idTransaction: Long) =
        transactionDao.removeTransactionByIdBalance(idBalance, idTransaction)
}
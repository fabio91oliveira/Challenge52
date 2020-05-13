package oliveira.fabio.challenge52.persistence.datasource

import oliveira.fabio.challenge52.persistence.model.entity.TransactionEntity

interface TransactionLocalDataSource {
    fun addTransaction(transactionEntity: TransactionEntity): Long
    fun removeTransactionByIdBalance(
        idBalance: Long,
        idTransaction: Long
    )
}
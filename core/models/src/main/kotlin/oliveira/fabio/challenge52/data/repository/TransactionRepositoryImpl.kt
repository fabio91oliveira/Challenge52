package oliveira.fabio.challenge52.data.repository

import oliveira.fabio.challenge52.data.mapper.TransactionEntityMapper
import oliveira.fabio.challenge52.domain.repository.TransactionRepository
import oliveira.fabio.challenge52.persistence.datasource.TransactionLocalDataSource
import oliveira.fabio.challenge52.presentation.vo.Transaction

internal class TransactionRepositoryImpl(
    private val transactionLocalDataSource: TransactionLocalDataSource,
    private val transactionEntityMapper: TransactionEntityMapper
) : TransactionRepository {
    override fun addTransaction(
        transaction: Transaction
    ) = transactionLocalDataSource.addTransaction(transactionEntityMapper(transaction))

    override fun removeTransactionByIdBalance(idBalance: Long, idTransaction: Long) =
        transactionLocalDataSource.removeTransactionByIdBalance(idBalance, idTransaction)
}
package oliveira.fabio.challenge52.domain.repository

import oliveira.fabio.challenge52.presentation.vo.Transaction

interface TransactionRepository {
    fun addTransaction(
        transaction: Transaction
    ): Long

    fun removeTransactionByIdBalance(
        idBalance: Long,
        idTransaction: Long
    )
}
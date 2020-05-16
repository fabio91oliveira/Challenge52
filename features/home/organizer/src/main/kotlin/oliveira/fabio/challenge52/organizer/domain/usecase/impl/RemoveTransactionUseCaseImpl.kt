package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.TransactionRepository
import oliveira.fabio.challenge52.organizer.domain.usecase.RemoveTransactionUseCase
import oliveira.fabio.challenge52.presentation.vo.Transaction

internal class RemoveTransactionUseCaseImpl(
    private val transactionRepository: TransactionRepository
) : RemoveTransactionUseCase {
    override suspend fun invoke(transaction: Transaction) = withContext(Dispatchers.IO) {
        delay(500)
        transactionRepository.removeTransactionByIdBalance(transaction.idBalance, transaction.id)
    }
}
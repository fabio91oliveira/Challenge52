package oliveira.fabio.challenge52.organizer.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.domain.repository.TransactionRepository
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateTransactionUseCase
import oliveira.fabio.challenge52.presentation.vo.Transaction

internal class CreateTransactionUseCaseImpl(
    private val transactionRepository: TransactionRepository
) : CreateTransactionUseCase {
    override suspend fun invoke(
        transaction: Transaction,
        idBalance: Long
    ) = withContext(Dispatchers.IO) {
        transaction.let {
            it.idBalance = idBalance
            transactionRepository.addTransaction(it)
        }
    }
}
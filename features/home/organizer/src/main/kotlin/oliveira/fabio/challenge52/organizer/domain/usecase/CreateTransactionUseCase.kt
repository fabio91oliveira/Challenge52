package oliveira.fabio.challenge52.organizer.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Transaction

interface CreateTransactionUseCase {
    suspend operator fun invoke(
        transaction: Transaction,
        idBalance: Long
    ): Long
}
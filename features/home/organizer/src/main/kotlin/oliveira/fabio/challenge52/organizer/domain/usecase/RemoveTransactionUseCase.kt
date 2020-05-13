package oliveira.fabio.challenge52.organizer.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Transaction

interface RemoveTransactionUseCase {
    suspend operator fun invoke(
        transaction: Transaction
    )
}
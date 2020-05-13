package oliveira.fabio.challenge52.organizer.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Balance
import oliveira.fabio.challenge52.presentation.vo.Transaction

interface UpdateBalanceAfterRemoveTransactionUseCase {
    suspend operator fun invoke(
        balance: Balance,
        transaction: Transaction
    )
}
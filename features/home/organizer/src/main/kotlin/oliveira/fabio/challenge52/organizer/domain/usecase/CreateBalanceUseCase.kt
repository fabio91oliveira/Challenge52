package oliveira.fabio.challenge52.organizer.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Balance

interface CreateBalanceUseCase {
    suspend operator fun invoke(
        balance: Balance
    ): Long
}